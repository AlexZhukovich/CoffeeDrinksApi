package com.alexzh.coffeedrinks

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.api.users.mapper.UserResponseMapper
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.moduleWithDependencies
import com.alexzh.coffeedrinks.testframework.mockconfig.MockConfigurator
import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import io.mockk.mockk

private const val JWT_ISSUER_ENV_KEY = "coffeeDrinksServer"
private const val JWT_SECRET_ENV_KEY = "jwt-12345678"
private const val HASH_KEY_ENV_KEY = "12345678"

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun <R> launchMockAppWithRealMappers(
    databaseConnector: DatabaseConnector = mockk(relaxed = true),
    coffeeDrinkRepository: CoffeeDrinkRepository = mockk(relaxed = true),
    coffeeDrinkMapper: CoffeeDrinkMapper = CoffeeDrinkMapper(),
    userRepository: UserRepository = mockk(relaxed = true),
    usersMapper: UserResponseMapper = UserResponseMapper(),
    jwtService: JwtService = mockk(relaxed = true),
    hashFunction: (String) -> String = mockk(relaxed = true),
    beforeTest: MockConfigurator.() -> Unit = { },
    test: TestApplicationEngine.() -> R
) {
    return launchMockApp(
            databaseConnector,
            coffeeDrinkRepository,
            coffeeDrinkMapper,
            userRepository,
            usersMapper,
            jwtService,
            hashFunction,
            beforeTest,
            test
    )
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun <R> launchMockApp(
    databaseConnector: DatabaseConnector = mockk(relaxed = true),
    coffeeDrinkRepository: CoffeeDrinkRepository = mockk(relaxed = true),
    coffeeDrinkMapper: CoffeeDrinkMapper = mockk(relaxed = true),
    userRepository: UserRepository = mockk(relaxed = true),
    usersMapper: UserResponseMapper = mockk(relaxed = true),
    jwtService: JwtService = mockk(relaxed = true),
    hashFunction: (String) -> String = mockk(relaxed = true),
    beforeTest: MockConfigurator.() -> Unit = { },
    test: TestApplicationEngine.() -> R
) {
    val appConfig = MockConfigurator(
            databaseConnector,
            coffeeDrinkRepository,
            coffeeDrinkMapper,
            userRepository,
            usersMapper,
            jwtService,
            hashFunction,
            JWT_ISSUER_ENV_KEY,
            JWT_SECRET_ENV_KEY
    ).apply { beforeTest() }
    withTestApplication({
        (environment.config as MapApplicationConfig).apply {
            // TODO: extract to separate place
            put("api.hashKey", HASH_KEY_ENV_KEY)
            put("api.jwtSecretKey", JWT_SECRET_ENV_KEY)
        }
        moduleWithDependencies(
                appConfig.databaseConnector,
                appConfig.coffeeDrinkRepository,
                appConfig.coffeeDrinkMapper,
                appConfig.userRepository,
                appConfig.userMapper,
                appConfig.jwtService,
                appConfig.hashFunction
        )
    }) {
        test()
    }
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

fun TestApplicationRequest.addFormParamHeader(
    boundary: String
) = addHeader(
    HttpHeaders.ContentType,
    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
)

// TODO: extract to separate file
fun formItem(key: String, value: String): PartData.FormItem {
    return PartData.FormItem(
        value,
        { },
        headersOf(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Inline
                .withParameter(ContentDisposition.Parameters.Name, key)
                .toString()
        )
    )
}