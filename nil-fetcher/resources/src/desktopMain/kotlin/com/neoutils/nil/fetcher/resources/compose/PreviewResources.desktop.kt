package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideSettings
import com.neoutils.nil.core.model.Settings

@Composable
actual fun ResourcesPreview(
    settings: Settings,
    content: @Composable () -> Unit
) {
    ProvideSettings(
        settings = settings,
        content = content,
    )
}