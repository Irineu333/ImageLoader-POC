package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.ProvideSettings
import com.neoutils.nil.core.model.Settings
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@Composable
actual fun ResourcesPreview(
    settings: Settings,
    content: @Composable () -> Unit
) {
    PreviewContextConfigurationEffect()

    ProvideSettings(
        settings = settings,
        content = content,
    )
}