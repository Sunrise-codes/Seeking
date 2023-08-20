package me.seeking.value

import java.util.function.Supplier

class Numbers<T : Number?> : Value<T> {
    var minimum: T
    var maximum: T
    var increment: T
    var dragged = false
    var isInteger = false
        private set

    constructor(name: String?, value: T, min: T, max: T, inc: T) : super(
        name!!, value,
        Supplier<Boolean> { true }) {
        handleInteger(inc)
        minimum = min
        maximum = max
        increment = inc
    }
    private fun handleInteger(inc: T) {
        if (inc!!.toDouble() % 1 == 0.0) isInteger = true
    }

    override var value
        get() = super.value
        set(value) {
            var value = value
            if (value != null) {
                if (value.toDouble() > maximum!!.toDouble()) value = maximum
            }
            if (value != null) {
                if (value.toDouble() < minimum!!.toDouble()) value = minimum
            }
            super.value = value as T
        }

    fun intValue(): Int {
        return value?.toInt() ?: 0
    }

    fun floatValue(): Float {
        return value?.toFloat() ?: 0F
    }
}
