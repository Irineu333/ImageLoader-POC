package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.painterCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifExtra
import com.neoutils.nil.decoder.gif.painter.MovieGifPainter
import com.neoutils.nil.type.Type

@Suppress("DEPRECATION")
class MovieGifDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportFormat())
        }

        return painterCatching {
            val params = extras[GifExtra.ExtrasKey]

            MovieGifPainter(
                movie = Movie.decodeStream(input.inputStream()),
                repeatCount = params.repeatCount
            )
        }
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.RECOMMEND
            else -> Support.NONE
        }
    }
}