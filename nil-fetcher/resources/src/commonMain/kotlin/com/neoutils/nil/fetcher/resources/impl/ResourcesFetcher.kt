package com.neoutils.nil.fetcher.resources.impl

import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.fetcher.resources.model.RequestResource
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourcesFetcher() : Fetcher<RequestResource>(RequestResource::class) {

    override fun fetch(
        input: RequestResource,
        extras: Extras
    ) = flow {
        emit(get(input, extras))
    }

    override suspend fun get(
        input: RequestResource,
        extras: Extras
    ) = resourceCatching {
        getDrawableResourceBytes(
            input.environment,
            input.res
        )
    }
}