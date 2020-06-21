package com.alexzh.coffeedrinks.api.api

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.api.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
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
    coffeeDrinkRepository: CoffeeDrinkRepository,
    mapper: CoffeeDrinkMapper
) {
    authenticate("jwt", optional = true) {
        get<CoffeeDrinksApi.AllCoffeeDrinks> {
            val user = call.principal<User>()

            // TODO: refactor code
            if (user == null) {
                val result = coffeeDrinkRepository.getCoffeeDrinks()
                call.respond(
                    result
                        .map { mapper.mapToCoffeeDrinkWithoutFavourite(it) }
                )
            } else {
                call.respond(
                    coffeeDrinkRepository.getCoffeeDrinksByUser(user.id)
                        .map { mapper.mapToCoffeeDrinkWithFavourite(it) }
                )
            }
        }
    }

    get<CoffeeDrinksApi.CoffeeDrinkById> { coffeeDrinkById ->
        val coffeeDrink = coffeeDrinkRepository.getCoffeeDrinkById(coffeeDrinkById.id)
        if (coffeeDrink != null) {
            call.respond(coffeeDrink)
        } else {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}