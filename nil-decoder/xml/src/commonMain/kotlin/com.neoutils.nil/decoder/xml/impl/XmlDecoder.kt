package com.neoutils.nil.decoder.xml.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.xml.painter.XmlDelegatePainter

private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

class XmlDecoder() : Decoder {

    override suspend fun decode(input: ByteArray): PainterResource.Result {
        return when (support(input)) {
            Support.NONE -> PainterResource.Result.Failure(NotSupportException())
            else -> PainterResource.Result.Success(XmlDelegatePainter(input))
        }
    }

    override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(VECTOR_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
