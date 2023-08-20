package me.seeking.value

import java.util.function.Supplier

/**
 * This file is a part Seeking
 */

open class Value<V>(name: String, value: V, visitable: Supplier<Boolean>) {
    private lateinit var name: String
    var value: V
    private val visitable: Supplier<Boolean>

    init {
        this.name = name
        this.visitable = visitable
        this.value = value
    }


    fun isVisitable(): Boolean {
        return visitable.get()
    }
}

