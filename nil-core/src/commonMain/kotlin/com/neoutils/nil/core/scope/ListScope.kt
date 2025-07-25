package com.neoutils.nil.core.scope

import kotlin.reflect.KClass

class ListScope<T : Any>(
    private var values: MutableList<T>
) {
    fun add(value: T) = values.add(value)

    fun remove(clazz: KClass<out T>) {
        values = values.filter {
            it::class != clazz
        }.toMutableList()
    }

    internal fun get() = values.toList()

    companion object {
        fun <T : Any> from(
            values: List<T>
        ) = ListScope(values.toMutableList())
    }
}
