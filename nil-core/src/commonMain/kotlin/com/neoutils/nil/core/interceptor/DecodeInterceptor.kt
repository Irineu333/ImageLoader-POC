package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

private val error = DecoderErrorStrings()

class DecodeInterceptor : Interceptor(Level.PAINTER) {

    override suspend fun sync(
        settings: Settings,
        chain: Chain
    ): Chain {
        if (!chain.painter.isLoading) return chain

        return chain.copy(
            painter = chain.data.toPainterResource { data ->
                settings
                    .decoderFor(data)
                    .toPainterResource { decoder ->
                        decoder.decode(
                            input = data,
                            extras = settings.extras,
                        )
                    }
            }
        )
    }

    private suspend fun Settings.decoderFor(bytes: ByteArray): Resource.Result<Decoder> {

        if (decoders.isEmpty()) {
            return Resource.Result.Failure(NoDecoderFound(error.notFound))
        }

        val decoder = decoders.maxBy { it.support(bytes) }

        if (decoder.support(bytes) == Support.NONE) {
            return Resource.Result.Failure(NoDecoderFound(error.notSupportedFound))
        }

        return Resource.Result.Success(decoder)
    }
}
