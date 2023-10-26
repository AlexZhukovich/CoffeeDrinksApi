package com.alexzh.coffeedrinks.api.routes

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.response.mapper.CoffeeDrinkMapper
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.coffeeDrinksRoutes(
    coffeeDrinksRepository: CoffeeDrinksRepository,
    mapper: CoffeeDrinkMapper
) {
    route("/api/v1/") {
        get("coffee-drinks") {
            val coffeeDrinks = coffeeDrinksRepository.getCoffeeDrinks()
            call.respond(
                coffeeDrinks.map { mapper.mapToCoffeeDrinkWithoutFavourite(it) }
            )
        }
    }
}