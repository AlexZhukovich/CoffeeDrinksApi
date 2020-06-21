package com.alexzh.coffeedrinks.api.auth

import com.alexzh.coffeedrinks.api.data.model.User
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.ApplicationEnvironment
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.util.*

@KtorExperimentalAPI
class JwtService(apiConfig: ApplicationConfig) {
    private val issuer = "coffeeDrinksServer"
    private val jwtSecret = apiConfig.property("jwtSecretKey").getString()
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    fun generateToken(user: User): String = JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("id", user.id)
            .withExpiresAt(expiresAt())
            .sign(algorithm)

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) // 24 hours
}