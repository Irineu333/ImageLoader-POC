package com.neoutils.nil.decoder.bitmap.impl

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.painterCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.type.Type
import org.jetbrains.compose.resources.decodeToImageBitmap

class BitmapDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormat())
        }

        return painterCatching {
            BitmapPainter(input.decodeToImageBitmap())
        }
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.PNG -> Support.RECOMMEND
            Type.JPEG -> Support.RECOMMEND
            Type.WEBP -> Support.SUPPORT
            Type.GIF -> Support.PARTIAL
            else -> Support.NONE
        }
    }
}

