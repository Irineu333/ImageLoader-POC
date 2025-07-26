package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.EmptyPainter
import com.neoutils.nil.core.painter.PainterResource

fun PainterResource.merge(
    failure: Painter = EmptyPainter,
    loading: Painter = EmptyPainter
): PainterResource {
    return when (this) {
        is PainterResource.Loading -> copy(painter = loading)
        is PainterResource.Result.Failure -> copy(painter = failure)
        is PainterResource.Result.Success -> this
    }
}

fun PainterResource.getOrElse(
    block: () -> PainterResource.Result
): PainterResource.Result {
    return when (this) {
        is PainterResource.Result.Success -> this
        else -> block()
    }
}
