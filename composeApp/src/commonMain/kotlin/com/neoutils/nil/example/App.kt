package com.neoutils.nil.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.composable.AsyncImage
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.decoder.svg.extension.svg
import com.neoutils.nil.decoder.xml.extension.xml
import com.neoutils.nil.fetcher.network.extension.network

@Composable
fun App() = AppTheme {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            request = Request.network("https://cataas.com/cat"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        ) {
            decoders {
                gif()
                xml()
                svg()
            }
        }
    }
}