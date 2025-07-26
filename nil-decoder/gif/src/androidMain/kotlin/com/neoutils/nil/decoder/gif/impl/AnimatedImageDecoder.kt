package com.neoutils.nil.decoder.gif.impl

import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.painterCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.ByteArrayKey
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.core.util.key
import com.neoutils.nil.decoder.gif.model.GifExtra
import com.neoutils.nil.decoder.gif.painter.AnimatedImageGifPainter
import com.neoutils.nil.decoder.gif.painter.DrawablePainter
import com.neoutils.nil.type.Type
import io.github.reactivecircus.cache4k.Cache

@RequiresApi(Build.VERSION_CODES.P)
class AnimatedImageDecoder : Decoder {

    private val cache = Cache.Builder<ByteArrayKey, Drawable>()
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

            when (val drawable = input.toDrawable()) {
                is AnimatedImageDrawable -> {
                    drawable.repeatCount = params.repeatCount

                    AnimatedImageGifPainter(drawable)
                }

                else -> {
                    DrawablePainter(drawable)
                }
            }
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

    private suspend fun ByteArray.toDrawable(): Drawable {
        return cache.get(key) {
            checkNotNull(
                AnimatedImageDrawable.createFromStream(inputStream(), null)
            )
        }
    }

    private suspend fun ByteArray.isAnimated() = runCatching {
        toDrawable() is AnimatedImageDrawable
    }.getOrElse {
        false
    }
}