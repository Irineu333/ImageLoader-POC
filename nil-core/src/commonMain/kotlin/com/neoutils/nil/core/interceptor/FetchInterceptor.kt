package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.map

private val error = FetcherErrorStrings()

class FetchInterceptor : Interceptor(Level.DATA) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {
        if (chain.painter != null) return ChainResult.Skip
        if (chain.data != null) return ChainResult.Skip

        return when (val fetcher = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                ChainResult.Process(
                    chain.doCopy(
                        data = Resource.Result.Failure(fetcher.throwable)
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> if chain is Chain.Async -> {
                ChainResult.Process(
                     fetcher.value.fetch(
                        input = chain.request,
                        extras = settings.extras
                    ).map { data ->
                        chain.copy(data = data)
                    }
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                ChainResult.Process(
                    chain.doCopy(
                        data = fetcher.value.get(
                            input = chain.request,
                            extras = settings.extras
                        )
                    )
                )
            }
        }
    }

    private fun Settings.fetcherFor(request: Request): Resource.Result<Fetcher<Request>> {

        if (fetchers.isEmpty()) {
            return Resource.Result.Failure(NoFetcherFound(error.notFound))
        }

        val fetcher = fetchers.find { it.type == request::class }

        return if (fetcher != null) {
            Resource.Result.Success(fetcher)
        } else {
            Resource.Result.Failure(NoFetcherFound(error.noRequiredFound))
        }
    }
}
