package com.neoutils.nil.core.composable

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.Request

@Composable
fun AsyncImage(
    request: Request,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = placeholder,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    settings: SettingsScope.() -> Unit = {}
) {
    val painter = asyncPainterResource(
        request = request,
        placeholder = placeholder,
        fallback = fallback,
        settings = settings
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}
