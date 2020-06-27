package com.alexzh.coffeedrinks.api.testframework.mockconfig.service

import com.alexzh.coffeedrinks.api.auth.JwtService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every

@KtorExperimentalAPI
class JwtServiceMockConfigurator(
        private val jwtIssuer: String,
        private val jwtSecret: String,
        private val jwtService: JwtService
) {
    @KtorExperimentalAPI
    fun stubAuthJwtVerifier() {
        // TODO: use external issuer and jwtSecret const
        val issuer = jwtIssuer
        val jwtSecret = jwtSecret
        val algorithm = Algorithm.HMAC512(jwtSecret)

        every { jwtService.verifier } returns
                JWT
                        .require(algorithm)
                        .withIssuer(issuer)
                        .build()
    }
}