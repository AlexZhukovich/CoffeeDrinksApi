package com.alexzh.coffeedrinks.api.feature.coffeedrinks.route

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.feature.auth.model.UserPrincipal
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.endpoint.CoffeeDrinks
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.param.CoffeeDrinkFavoriteRequestParam
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkFavoriteResponse
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
                        .map { mapper.mapToCoffeeDrinkWithoutFavorite(it) }
                )
            } else {
                call.respond(
                    coffeeDrinksRepository.getCoffeeDrinksByUser(user.user.id)
                        .map { mapper.mapToCoffeeDrinkWithFavorite(it) }
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
                        mapper.mapToCoffeeDrinkWithoutFavorite(foundCoffeeDrink)
                    } else {
                        mapper.mapToCoffeeDrinkWithFavorite(foundCoffeeDrink)
                    }
                )
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    authenticate("jwt") {
        patch<CoffeeDrinks.FavoriteCoffeeDrink> {
            val user = call.principal<UserPrincipal>()
            val params = runCatching { call.receive<CoffeeDrinkFavoriteRequestParam>() }.getOrElse {
                throw BadRequestException("The 'isFavorite' parameter is required")
            }
            if (user != null) {
                call.respond(CoffeeDrinkFavoriteResponse(
                    coffeeDrinksRepository.updateFavoriteStateOfCoffeeForUser(user.user.id, it.id, params.isFavorite)
                ))
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}