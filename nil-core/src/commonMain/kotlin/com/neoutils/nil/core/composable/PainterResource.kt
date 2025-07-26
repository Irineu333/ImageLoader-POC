@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.composable

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.annotation.NilDsl
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.merge
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.painter.Animatable
import com.neoutils.nil.core.painter.EmptyPainter
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.scope.SettingsScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@Composable
fun asyncPainterResource(
    request: Request,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: @NilDsl SettingsScope.() -> Unit = {}
): PainterResource {

    val settings = rememberSettings(settings)

    val nil = remember(settings) { Nil(settings) }

    val flow = remember(nil, request) { nil.async(request) }

    val painter by flow.collectAsState(PainterResource.Loading())

    return rememberPainterResource(
        resource = painter,
        placeholder = placeholder,
        fallback = fallback
    )
}

@Composable
fun painterResource(
    request: Request.Sync,
    settings: @NilDsl SettingsScope.() -> Unit = {}
): PainterResource {

    val settings = rememberSettings(settings)

    val nil = remember(settings) { Nil(settings) }

    val painter = remember(nil, request) { nil.sync(request) }

    return rememberPainterResource(painter)
}

@Composable
private fun rememberPainterResource(
    resource: PainterResource,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter
): PainterResource {

    val resource = remember(resource, placeholder, fallback) {
        resource.merge(
            failure = fallback,
            loading = placeholder
        )
    }

    LaunchedEffect(resource) {
        when (val painter = resource.painter) {
            is Animatable -> {
                withContext(Dispatchers.Default) {
                    painter.animate()
                }
            }
        }
    }

    return resource
}
