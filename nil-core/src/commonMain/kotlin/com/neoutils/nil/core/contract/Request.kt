package com.neoutils.nil.core.contract

import com.neoutils.nil.core.model.Resize

abstract class Request private constructor() {
    abstract val resize: Resize?
    abstract class Async : Request()
    abstract class Sync : Request()

    companion object
}
