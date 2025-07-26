package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.rememberSettings
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.fetcher.resources.impl.ResourcesFetcher

@Composable
expect fun ResourcesPreview(
    settings: Settings = rememberSettings {
        fetchers {
            add(ResourcesFetcher())
        }
    },
    content: @Composable () -> Unit
)