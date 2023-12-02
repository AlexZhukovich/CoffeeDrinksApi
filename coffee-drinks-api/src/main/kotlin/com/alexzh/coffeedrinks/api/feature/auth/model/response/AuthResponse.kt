package com.alexzh.coffeedrinks.api.feature.auth.model.response

import io.ktor.http.*

sealed class AuthResponse(val status: HttpStatusCode) {

    class Created(
        val token: String
    ): AuthResponse(HttpStatusCode.Created)

    class Success(
        val token: String?
    ): AuthResponse(HttpStatusCode.OK)

    class Error(
        val message: String
    ): AuthResponse(HttpStatusCode.BadRequest)
}
