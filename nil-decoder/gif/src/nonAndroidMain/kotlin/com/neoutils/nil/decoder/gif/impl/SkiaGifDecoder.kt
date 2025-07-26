package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.painterCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.ByteArrayKey
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.core.util.key
import com.neoutils.nil.decoder.gif.model.GifExtra
import com.neoutils.nil.decoder.gif.painter.SkiaGifPainter
import com.neoutils.nil.type.Type
import io.github.reactivecircus.cache4k.Cache
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class SkiaGifDecoder : Decoder {

    private val cache = Cache.Builder<ByteArrayKey, Codec>()
        .maximumCacheSize(size = 1)
        .build()

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormat())
        }

        return painterCatching {
            val params = extras[GifExtra.ExtrasKey]

            val codec = cache.get(input.key) {
                Codec.makeFromData(Data.makeFromBytes(input))
            }

            SkiaGifPainter(
                codec = codec,
                repeatCount = params.repeatCount
            )
        }
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.RECOMMEND
            Type.WEBP if input.isAnimated() -> Support.RECOMMEND
            Type.WEBP, Type.PNG, Type.JPEG -> Support.SUPPORT
            else -> Support.NONE
        }
    }

   private suspend fun ByteArray.isAnimated() = runCatching {

        val codec = cache.get(key) {
            Codec.makeFromData(Data.makeFromBytes(this))
        }

        codec.framesInfo.isNotEmpty()
    }.getOrElse {
        false
    }
}
