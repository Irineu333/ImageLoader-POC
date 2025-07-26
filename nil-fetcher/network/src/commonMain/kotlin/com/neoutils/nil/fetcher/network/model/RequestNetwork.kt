package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import io.ktor.http.*

data class RequestNetwork(
    val url: String,
    val method: HttpMethod,
    override val key: String = url
) : Request.Async(), Cacheable
