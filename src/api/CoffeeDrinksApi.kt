package com.alexzh.coffeedrinks.api.api

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

const val COFFEE_DRINKS_ENDPOINT = "$API_VERSION/coffee-drinks"
const val COFFEE_DRINK_BY_ID_ENDPOINT = "$COFFEE_DRINKS_ENDPOINT/{id}"

@KtorExperimentalLocationsAPI
object CoffeeDrinksApi {
    @Location(COFFEE_DRINKS_ENDPOINT)
    class AllCoffeeDrinks

    @Location(COFFEE_DRINK_BY_ID_ENDPOINT)
    class CoffeeDrinkById(val id: Long)
}

@KtorExperimentalLocationsAPI
fun Route.coffeeDrinks(
    coffeeDrinkRepository: CoffeeDrinkRepository
) {
    get<CoffeeDrinksApi.AllCoffeeDrinks> {
        call.respond(
            coffeeDrinkRepository.getCoffeeDrinks()
        )
    }
    get<CoffeeDrinksApi.CoffeeDrinkById> { coffeeDrinkById ->
        val coffeeDrink = coffeeDrinkRepository.getCoffeeDrinkById(coffeeDrinkById.id)
        if (coffeeDrink != null) {
            call.respond(coffeeDrink)
        }
        // TODO: improve behaviour
    }
}