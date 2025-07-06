package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okio.ByteString.Companion.encodeUtf8

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

    private val caches = mutableMapOf<DiskCacheExtra, LruDiskCache>()

    private val Request.hash
        get() = when (this) {
            is Cacheable -> {
                key.encodeUtf8().md5().hex()
            }

            else -> null
        }

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val key = chain.request.hash ?: return flowOf(chain)

        val extra = settings.extras[DiskCacheExtra.ExtrasKey]

        val cache = caches.getOrPut(extra) {
            LruDiskCache(
                fileSystem = extra.fileSystem,
                path = extra.path,
                maxSize = extra.maxSize
            )
        }

        return flowOf(
            when (val data = chain.data) {
                is Resource.Result.Success<ByteArray> if extra.enabled -> {
                    cache[key] = data.value
                    chain
                }

                is Resource.Loading if extra.enabled && cache.has(key) -> {
                    chain.copy(
                        data = Resource.Result.Success(cache[key])
                    )
                }

                else -> chain
            }
        )
    }
}
