package me.seeking.value

import java.util.function.Supplier

/**
 * This file is a part o Seeking
 */

class Option(name: String?, enabled: Boolean?) : Value<Boolean?>(name!!, enabled, Supplier<Boolean> { true }) {
    fun getValue(): Boolean {
        return isVisitable() && super.value!!
    }
}

