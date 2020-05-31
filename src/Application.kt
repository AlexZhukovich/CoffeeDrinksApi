package com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.api.coffeeDrinks
import com.alexzh.coffeedrinks.api.data.database.DatabaseFactory
import com.alexzh.coffeedrinks.api.data.repository.MySQLCoffeeDrinkRepository
import io.ktor.application.Application
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
import io.ktor.response.respondText
import io.ktor.routing.routing

const val API_VERSION = "/api/v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Reference in application.conf
@JvmOverloads
@KtorExperimentalLocationsAPI
fun Application.module(testing: Boolean = false) {
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

    DatabaseFactory.init()
    val repository = MySQLCoffeeDrinkRepository()

    routing {
        coffeeDrinks(repository)
    }
}