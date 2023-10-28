package com.alexzh.coffeedrinks.api.plugins

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.route.coffeeDrinksRoutes
import com.alexzh.coffeedrinks.api.feature.unauthorized.route.unauthorizedRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    val coffeeDrinksRepository: CoffeeDrinksRepository by inject()
    val coffeeDrinkResponseMapper: CoffeeDrinkResponseMapper by inject()

    install(Locations)

    routing {
        coffeeDrinksRoutes(coffeeDrinksRepository, coffeeDrinkResponseMapper)
        unauthorizedRoute()

        staticResources("/", "static")
    }
}
