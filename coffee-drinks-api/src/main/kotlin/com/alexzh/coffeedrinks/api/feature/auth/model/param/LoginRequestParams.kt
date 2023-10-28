package com.alexzh.coffeedrinks.api.feature.auth.model.param

import com.alexzh.coffeedrinks.api.common.model.Email
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestParams(
    val email: Email,
    val password: String
)
