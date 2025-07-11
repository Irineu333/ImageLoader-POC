package com.neoutils.nil.core.util

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.core.foundation.Interceptor
import java.util.*

object Dynamic {
    val decoders = load<Decoder>()
    val fetchers = load<Fetcher<*>>()
    val interceptors = load<Interceptor>()
}

private inline fun <reified T : Any> load(): List<T> = ServiceLoader.load(
    T::class.java,
    T::class.java.classLoader,
).toList()