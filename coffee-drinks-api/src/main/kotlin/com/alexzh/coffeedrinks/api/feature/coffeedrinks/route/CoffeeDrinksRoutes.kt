package com.alexzh.coffeedrinks.api.feature.coffeedrinks.route

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.endpoint.CoffeeDrinks
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.coffeeDrinksRoutes(
    coffeeDrinksRepository: CoffeeDrinksRepository,
    mapper: CoffeeDrinkResponseMapper
) {
    get<CoffeeDrinks> {
        val coffeeDrinks = coffeeDrinksRepository.getCoffeeDrinks()
        call.respond(
            coffeeDrinks.map { mapper.mapToCoffeeDrinkWithoutFavourite(it) }
        )
    }
}