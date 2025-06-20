package com.neoutils.nil.decoder.lottie.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.DelegatePainter
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

class LottieComposePainter(
    private val spec: LottieCompositionSpec
) : DelegatePainter() {

    @Composable
    override fun delegate(): Painter {

        val composition by rememberLottieComposition { spec }

        val progress by animateLottieCompositionAsState(composition)

        return rememberLottiePainter(
            composition = composition,
            progress = { progress },
        )
    }
}