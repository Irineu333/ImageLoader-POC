package com.neoutils.nil.decoder.svg.impl

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayOutputStream

class AndroidSvgDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): Resource.Result<Painter> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportFormat())
        }

        return resourceCatching {
            val transcoderInput = TranscoderInput(input.inputStream())
            val outputStream = ByteArrayOutputStream()
            val transcoderOutput = TranscoderOutput(outputStream)

            val transcoder = PNGTranscoder()
            transcoder.addTranscodingHint(PNGTranscoder.KEY_ALLOW_EXTERNAL_RESOURCES, true)
            transcoder.transcode(transcoderInput, transcoderOutput)

            val byteArray = outputStream.toByteArray()
            val bitmap = org.jetbrains.skia.Image.makeFromEncoded(byteArray).asImageBitmap()
            BitmapPainter(bitmap)
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
