package com.neoutils.nil.core.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Level

import com.neoutils.nil.core.interceptor.ResizeInterceptor

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    ResizeInterceptor(),
    DecodeInterceptor()
)

val LocalInterceptors = staticCompositionLocalOf {
    DefaultInterceptors + Dynamic.interceptors
}

abstract class Interceptor(vararg val levels: Level) {
    abstract suspend fun intercept(
        settings: Settings,
        chain: Chain,
    ): ChainResult
}
