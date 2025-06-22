package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Resource

fun Result<Painter>.toPainterResource(): PainterResource.Result {
    return if (isSuccess) {
        PainterResource.Result.Success(getOrThrow())
    } else {
        PainterResource.Result.Failure(checkNotNull(exceptionOrNull()))
    }
}
fun <T> Result<T>.toResource(): Resource.Result<T> {
    return if (isSuccess) {
        Resource.Result.Success(getOrThrow())
    } else {
        Resource.Result.Failure(checkNotNull(exceptionOrNull()))
    }
}
