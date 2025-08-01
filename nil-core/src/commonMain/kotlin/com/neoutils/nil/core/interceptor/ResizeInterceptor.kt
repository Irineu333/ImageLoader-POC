package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.image.resizeImage
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource

class ResizeInterceptor : Interceptor(Level.Decode) {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain,
    ): ChainResult {
        val request = chain.request
        val resize = request.resize
        val data = chain.data

        if (resize == null || data == null) {
            return ChainResult.Skip
        }

        val resizedData = when (data) {
            is Resource.Result -> {
                val resized = resizeImage(data.value, resize)
                Resource.Result(resized)
            }

            else -> data
        }

        return ChainResult.Process(
            chain.doCopy(
                data = resizedData
            )
        )
    }
}
