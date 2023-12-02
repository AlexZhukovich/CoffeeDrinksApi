package com.alexzh.coffeedrinks.api.feature.auth.model.param

import com.alexzh.coffeedrinks.api.common.model.Email
import com.alexzh.coffeedrinks.api.common.model.Name
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequestParams(
    val username: Name,
    val email: Email,
    val password: String
)
