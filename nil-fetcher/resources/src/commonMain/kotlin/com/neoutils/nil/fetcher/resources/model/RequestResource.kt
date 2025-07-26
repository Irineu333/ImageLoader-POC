package com.neoutils.nil.fetcher.resources.model

import com.neoutils.nil.core.contract.Request
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment

data class RequestResource(
    val res: DrawableResource,
    val environment: ResourceEnvironment
) : Request.Sync()
