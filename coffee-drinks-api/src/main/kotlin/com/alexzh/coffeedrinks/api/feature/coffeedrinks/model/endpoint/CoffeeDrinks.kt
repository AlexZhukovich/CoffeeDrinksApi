package com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.endpoint

import com.alexzh.coffeedrinks.api.common.BASE_URL
import io.ktor.server.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
object CoffeeDrinks {

    @Location("${BASE_URL}/coffee-drinks")
    class AllCoffeeDrinks()

    @Location("${BASE_URL}/coffee-drinks/{id}")
    class CoffeeDrinkById(val id: Long)

    @Location("${BASE_URL}/coffee-drinks/{id}")
    class FavouriteCoffeeDrink(
        val id: Long
    )
}
