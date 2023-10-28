package com.alexzh.coffeedrinks.api.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = -1L,
    val name: String,
    val email: String,
    val passwordHash: String
)
