@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.foundation.async
import com.neoutils.nil.core.foundation.sync
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

class Nil(
    private val settings: Settings
) {
    private val interceptors = Level.entries.flatMap { level ->
        settings.interceptors.filter {
            it.levels.contains(level)
        }
    }

    fun async(request: Request): Flow<PainterResource> {

        return interceptors.fold(
            flowOf<Chain>(Chain.Async(request))
        ) { flow, interceptor ->
            flow.flatMapConcat { chain ->
                interceptor.async(settings, chain)
            }
        }.state
    }

    fun sync(request: Request.Sync) = runBlocking {
        interceptors.fold(
            Chain.Sync(request)
        ) { chain, interceptor ->
            interceptor.sync(settings, chain)
        }.result
    }
}
