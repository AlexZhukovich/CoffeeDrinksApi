package com.alexzh.coffeedrinks.api.plugins

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.response.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.routes.coffeeDrinksRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val coffeeDrinksRepository: CoffeeDrinksRepository by inject()
    val coffeeDrinkMapper: CoffeeDrinkMapper by inject()

    routing {
        coffeeDrinksRoutes(coffeeDrinksRepository, coffeeDrinkMapper)
        staticResources("/", "static")
    }
}
