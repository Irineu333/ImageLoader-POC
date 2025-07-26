package com.neoutils.nil.interceptor.memoryCache.impl

import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.interceptor.memoryCache.model.MemoryCacheExtra
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache
import com.neoutils.nil.util.Remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MemoryCacheInterceptor : Interceptor(Level.REQUEST, Level.PAINTER) {

    private val caches = Remember<LruMemoryCache>()

    override suspend fun sync(
        settings: Settings,
        chain: Chain
    ): Chain {

        val extra = settings.extras[MemoryCacheExtra.ExtrasKey]

        val cache = caches(extra) {
            LruMemoryCache(
                maxSize = extra.maxSize
            )
        }

        return when (chain.painter) {
            is PainterResource.Result.Success if extra.enabled -> {
                cache[chain.request] = chain.painter
                chain
            }

            is PainterResource.Loading if extra.enabled && cache.has(chain.request) -> {
                chain.copy(
                    painter = cache[chain.request]
                )
            }

            else -> chain
        }
    }
}
