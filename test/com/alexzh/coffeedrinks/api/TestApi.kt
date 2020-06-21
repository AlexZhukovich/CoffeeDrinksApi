package com.alexzh.coffeedrinks.api.com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.api.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.moduleWithDependencies
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.config.MapApplicationConfig
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockk

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun <R> launchTestApp(
        databaseConnector: DatabaseConnector = mockk(relaxed = true),
        coffeeDrinkRepository: CoffeeDrinkRepository = mockk(relaxed = true),
        userRepository: UserRepository = mockk(relaxed = true),
        coffeeDrinkMapper: CoffeeDrinkMapper = mockk(relaxed = true),
        jwtService: JwtService = mockk(relaxed = true),
        hashFunction: (String) -> String = mockk(relaxed = true),
        test: TestApplicationEngine.() -> R
) : R {
    return withTestApplication({
        (environment.config as MapApplicationConfig).apply {
            // TODO: extract to separate place
            put("api.hashKey", "12345678")
            put("api.jwtSecretKey", "jwt-12345678")
        }
        moduleWithDependencies(
                databaseConnector,
                coffeeDrinkRepository,
                userRepository,
                coffeeDrinkMapper,
                jwtService,
                hashFunction
        )
    }) {
        test()
    }
}

@KtorExperimentalAPI
fun stubAuthVerifier(jwtService: JwtService) {
    // TODO: use external issuer and jwtSecret const
    val issuer = "coffeeDrinksServer"
    val jwtSecret = "jwt-12345678"
    val algorithm = Algorithm.HMAC512(jwtSecret)

    every { jwtService.verifier } returns
        JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()
}

// TODO: improve this function
//  - should use JwtService dependency
@KtorExperimentalAPI
fun TestApplicationRequest.addAuthHeader(
    testAppEngine: TestApplicationEngine,
    user: User
) = addHeader(
    "Authorization",
    "Bearer ${JwtService(testAppEngine.environment.config.config("api")).generateToken(user)}"
)

// TODO: add methods for custom assertion with status code and object of data