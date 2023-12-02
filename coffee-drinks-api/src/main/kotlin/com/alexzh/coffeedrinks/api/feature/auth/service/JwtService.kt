package com.alexzh.coffeedrinks.api.feature.auth.service

import com.auth0.jwt.JWTVerifier

interface JwtService {
    val claim: String

    val verifier: JWTVerifier

    fun generateToken(userId: Long): String
}