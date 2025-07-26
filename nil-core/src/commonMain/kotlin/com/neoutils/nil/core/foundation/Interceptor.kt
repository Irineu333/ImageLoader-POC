package com.neoutils.nil.core.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import com.neoutils.nil.core.interceptor.DecodeInterceptor
import com.neoutils.nil.core.interceptor.FetchInterceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val DefaultInterceptors = listOf(
    FetchInterceptor(),
    DecodeInterceptor()
)

val LocalInterceptors = staticCompositionLocalOf {
    DefaultInterceptors + Dynamic.interceptors
}

abstract class Interceptor(vararg val levels: Level) {

    open fun async(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> = flow {
        emit(sync(settings, chain))
    }

    abstract suspend fun sync(
        settings: Settings,
        chain: Chain
    ): Chain
}
