package com.neoutils.nil.example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ImageLoader-POC",
    ) {
        App()
    }
}