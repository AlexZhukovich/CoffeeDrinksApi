package com.alexzh.coffeedrinks.api.api

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.repository.CoffeeDrinkRepository
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

const val COFFEE_DRINKS_ENDPOINT = "$API_VERSION/coffee-drinks"
const val ALL_COFFEE_DRINKS = "$COFFEE_DRINKS_ENDPOINT/"
const val COFFEE_DRINK_BY_ID = "$COFFEE_DRINKS_ENDPOINT/{id}"

@KtorExperimentalLocationsAPI
@Location(COFFEE_DRINKS_ENDPOINT)
class CoffeeDrinksApi {
    @Location(ALL_COFFEE_DRINKS)
    class All(val parent: CoffeeDrinksApi)

    @Location(COFFEE_DRINK_BY_ID)
    class ById(val parent: CoffeeDrinksApi, val id: Long)
}

@KtorExperimentalLocationsAPI
fun Route.coffeeDrinks(
    coffeeDrinkRepository: CoffeeDrinkRepository
) {
    get<CoffeeDrinksApi.All> {
        call.respond(
            coffeeDrinkRepository.getCoffeeDrinks()
        )
    }
    get<CoffeeDrinksApi.ById> { coffeeDrinkById ->
        val coffeeDrink = coffeeDrinkRepository.getCoffeeDrinkById(coffeeDrinkById.id)
        if (coffeeDrink != null) {
            call.respond(coffeeDrink)
        }
        // TODO: improve behaviour
    }
}