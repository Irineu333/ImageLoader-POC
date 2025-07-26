package com.neoutils.nil.decoder.svg.impl

import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.extension.painterCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.AndroidSvgPainter

class AndroidSvgDecoder() : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormat())
        }

        return painterCatching {

            val density = extras[DensityExtrasKey]

            AndroidSvgPainter(SVG.getFromInputStream(input.inputStream()), density)
        }
    }

    override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.RECOMMEND
        }

        return Support.NONE
    }
}

