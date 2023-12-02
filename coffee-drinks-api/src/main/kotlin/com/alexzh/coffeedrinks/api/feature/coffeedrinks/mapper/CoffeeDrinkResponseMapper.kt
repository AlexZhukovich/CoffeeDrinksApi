package com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkWithFavoriteResponse
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkWithoutFavoriteResponse

class CoffeeDrinkResponseMapper {

    fun mapToCoffeeDrinkWithFavorite(
        coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithFavoriteResponse(
        coffeeDrink.id,
        coffeeDrink.name,
        coffeeDrink.imageUrl,
        coffeeDrink.description,
        coffeeDrink.ingredients,
        coffeeDrink.isFavorite
    )

    fun mapToCoffeeDrinkWithoutFavorite(
        coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithoutFavoriteResponse(
        coffeeDrink.id,
        coffeeDrink.name,
        coffeeDrink.imageUrl,
        coffeeDrink.description,
        coffeeDrink.ingredients
    )
}