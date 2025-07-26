package com.neoutils.nil.core.model

import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Chain(
    val request: Request,
    val data: Resource<ByteArray> = Resource.Loading(),
    val painter: PainterResource = PainterResource.Loading(),
)

val Chain.result get() =  when(painter) {
    is PainterResource.Loading -> throw RuntimeException()
    is PainterResource.Result.Failure -> painter
    is PainterResource.Result.Success -> painter
}

val Flow<Chain>.state get() = map { it.painter }
