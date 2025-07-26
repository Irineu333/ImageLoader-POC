package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoFetcherFound
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.strings.FetcherErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private val error = FetcherErrorStrings()

class FetchInterceptor : Interceptor(Level.DATA) {

    override suspend fun sync(settings: Settings, chain: Chain): Chain {

        if (!chain.painter.isLoading) return chain
        if (!chain.data.isLoading) return chain

        return when (val fetcher = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                chain.copy(
                    data = Resource.Result.Failure(
                        fetcher.throwable
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                chain.copy(
                    data = fetcher.value.get(
                        input = chain.request,
                        extras = settings.extras
                    )
                )
            }
        }
    }

    override fun async(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        if (!chain.painter.isLoading) return flowOf(chain)
        if (!chain.data.isLoading) return flowOf(chain)

        return when (val fetcher = settings.fetcherFor(chain.request)) {
            is Resource.Result.Failure -> {
                flowOf(
                    chain.copy(
                        data = Resource.Result.Failure(
                            fetcher.throwable
                        )
                    )
                )
            }

            is Resource.Result.Success<Fetcher<Request>> -> {
                fetcher.value.fetch(
                    input = chain.request,
                    extras = settings.extras
                ).map { data ->
                    chain.copy(data = data)
                }
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
