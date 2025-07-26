package com.neoutils.nil.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.composable.asyncPainterResource
import com.neoutils.nil.core.composable.painterResource
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.decoder.gif.extension.gif
import com.neoutils.nil.decoder.svg.extension.svg
import com.neoutils.nil.decoder.xml.extension.xml
import com.neoutils.nil.example.resources.Res
import com.neoutils.nil.example.resources.atom
import com.neoutils.nil.example.resources.atom_vector
import com.neoutils.nil.example.resources.crazy_cat
import com.neoutils.nil.fetcher.network.extension.network
import com.neoutils.nil.fetcher.resources.extension.resource
import com.neoutils.nil.interceptor.diskcache.extension.diskCache

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = painterResource(
        request = Request.resource(Res.drawable.crazy_cat),
    ) {
        decoders {
            xml()
            svg()
            gif()
        }
    }

    when (resource) {
        is PainterResource.Result.Success -> {
            Image(
                painter = resource,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        is PainterResource.Loading -> {
            if (resource.progress != null) {
                CircularProgressIndicator(
                    progress = { resource.progress!! }
                )
            } else {
                CircularProgressIndicator()
            }
        }

        is PainterResource.Result.Failure -> {
            Text(resource.throwable.message.orEmpty())
        }
    }
}
