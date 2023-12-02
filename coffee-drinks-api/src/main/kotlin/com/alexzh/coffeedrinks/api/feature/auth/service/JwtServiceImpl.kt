package com.alexzh.coffeedrinks.api.feature.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

class JwtServiceImpl(
    secretKey: String
): JwtService {
    private val algorithm = Algorithm.HMAC512(secretKey)
    private val issuer = "coffee-drinks-server"

    override val claim = "id"

    override val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun generateToken(userId: Long): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim(claim, userId)
            .sign(algorithm)
    }
}