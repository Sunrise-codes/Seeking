package me.seeking.value

import java.util.function.Supplier

/**
 * This file is a part Seeking
 */

open class Value<V>(name: String, value: V, visitable: Supplier<Boolean>) {
    private lateinit var name: String
    open var value: V = value
    private val visitable: Supplier<Boolean>

    init {
        this.name = name
        this.visitable = visitable
    }


    fun isVisitable(): Boolean {
        return visitable.get()
    }
}

