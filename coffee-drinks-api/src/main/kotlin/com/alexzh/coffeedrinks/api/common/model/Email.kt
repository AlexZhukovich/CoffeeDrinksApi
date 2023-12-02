package com.alexzh.coffeedrinks.api.common.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Email(
    val value: String
) {
    val length: Int
        get() = value.length

    fun isValid(): Boolean {
        val pattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$".toRegex()
        return value.matches(pattern)
    }
}