package com.alexzh.coffeedrinks.api.feature.coffeedrinks.route

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.feature.auth.model.UserPrincipal
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.endpoint.CoffeeDrinks
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.param.CoffeeDrinkFavouriteRequestParam
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkFavouriteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.coffeeDrinksRoutes(
    coffeeDrinksRepository: CoffeeDrinksRepository,
    mapper: CoffeeDrinkResponseMapper
) {
    authenticate("jwt", optional = true) {
        get<CoffeeDrinks.AllCoffeeDrinks> {
            val user = call.principal<UserPrincipal>()

            if (user == null) {
                call.respond(
                    coffeeDrinksRepository.getCoffeeDrinks()
                        .map { mapper.mapToCoffeeDrinkWithoutFavourite(it) }
                )
            } else {
                call.respond(
                    coffeeDrinksRepository.getCoffeeDrinksByUser(user.user.id)
                        .map { mapper.mapToCoffeeDrinkWithFavourite(it) }
                )
            }
        }

        get<CoffeeDrinks.CoffeeDrinkById> {
            val user = call.principal<UserPrincipal>()
            val foundCoffeeDrink = if (user == null) {
                coffeeDrinksRepository.getCoffeeDrinkById(it.id)
            } else {
                coffeeDrinksRepository.getCoffeeDrinkByUserAndCoffeeDrinkId(user.user.id, it.id)
            }
            if (foundCoffeeDrink != null) {
                call.respond(
                    if (user == null) {
                        mapper.mapToCoffeeDrinkWithoutFavourite(foundCoffeeDrink)
                    } else {
                        mapper.mapToCoffeeDrinkWithFavourite(foundCoffeeDrink)
                    }
                )
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    authenticate("jwt") {
        patch<CoffeeDrinks.FavouriteCoffeeDrink> {
            val user = call.principal<UserPrincipal>()
            val params = runCatching { call.receive<CoffeeDrinkFavouriteRequestParam>() }.getOrElse {
                throw BadRequestException("The 'isFavourite' parameter is required")
            }
            if (user != null) {
                call.respond(CoffeeDrinkFavouriteResponse(
                    coffeeDrinksRepository.updateFavouriteStateOfCoffeeForUser(user.user.id, it.id, params.isFavourite)
                ))
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}