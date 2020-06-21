package com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.api.AppSession
import com.alexzh.coffeedrinks.api.api.coffeedrinks.coffeeDrinks
import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.api.users
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.auth.hash
import com.alexzh.coffeedrinks.api.auth.hashKey
import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.database.MySQLDatabaseConnector
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.MySQLCoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.MySQLUserRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.locations.locations
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI

const val API_VERSION = "/api/v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Reference in application.conf
@JvmOverloads
@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module(testing: Boolean = false) {
    val apiConfig = environment.config.config("api")

    val databaseConnector = MySQLDatabaseConnector()
    val coffeeDrinksRepository = MySQLCoffeeDrinkRepository()
    val userRepository = MySQLUserRepository()
    val coffeeDrinkMapper = CoffeeDrinkMapper()
    val jwtService = JwtService(apiConfig)

    val hashFunction = { s: String -> hash(environment, s)}

    moduleWithDependencies(
        databaseConnector,
        coffeeDrinksRepository,
        userRepository,
        coffeeDrinkMapper,
        jwtService,
        hashFunction
    )
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.moduleWithDependencies(
        databaseConnector: DatabaseConnector,
        coffeeDrinkRepository: CoffeeDrinkRepository,
        userRepository: UserRepository,
        coffeeDrinkMapper: CoffeeDrinkMapper,
        jwtService: JwtService,
        hashFunction: (String) -> String
) {
    install(DefaultHeaders)
    install(StatusPages) {
        exception<Throwable> { ex ->
            call.respondText(
                ex.localizedMessage,
                ContentType.Text.Plain,
                HttpStatusCode.InternalServerError
            )
        }
    }
    install(ContentNegotiation) {
        gson()
    }
    install(Locations)
    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "Coffee Drinks server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val userId = claim.asLong()
                val user = userRepository.getUserById(userId)
                user
            }
        }
    }
    install(Sessions) {
        cookie<AppSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey(environment)))
        }
    }

    databaseConnector.connect()

    routing {
        coffeeDrinks(coffeeDrinkRepository, coffeeDrinkMapper)
        users(userRepository, jwtService, hashFunction)
    }
}

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}