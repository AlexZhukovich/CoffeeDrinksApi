package com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.api.coffeeDrinks
import com.alexzh.coffeedrinks.api.api.users
import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.database.MySQLDatabaseConnector
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.MySQLCoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.MySQLUserRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
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

const val API_VERSION = "/api/v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Reference in application.conf
@JvmOverloads
@KtorExperimentalLocationsAPI
fun Application.module(testing: Boolean = false) {
    val databaseConnector = MySQLDatabaseConnector()
    val coffeeDrinksRepository = MySQLCoffeeDrinkRepository()
    val userRepository = MySQLUserRepository()

    moduleWithDependencies(
        databaseConnector,
        coffeeDrinksRepository,
        userRepository
    )
}

@KtorExperimentalLocationsAPI
fun Application.moduleWithDependencies(
    databaseConnector: DatabaseConnector,
    coffeeDrinkRepository: CoffeeDrinkRepository,
    userRepository: UserRepository
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

    databaseConnector.connect()

    routing {
        coffeeDrinks(coffeeDrinkRepository)
        users(userRepository)
    }
}

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}