package com.neoutils.nil.fetcher.network.model

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import io.ktor.http.*

import com.neoutils.nil.core.model.Resize

data class RequestNetwork(
    val url: String,
    val method: HttpMethod,
    override val resize: Resize? = null,
) : Request.Async(), Cacheable {
    override val key: String
        get() = if (resize != null) {
            "${url}_w${resize.width}_h${resize.height}"
        } else {
            url
        }
}
