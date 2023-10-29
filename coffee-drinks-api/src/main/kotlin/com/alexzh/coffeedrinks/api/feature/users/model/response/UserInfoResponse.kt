package com.alexzh.coffeedrinks.api.feature.users.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userId: Long,
    val name: String,
    val email: String
)
