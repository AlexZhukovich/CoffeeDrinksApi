package com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkWithFavouriteResponse
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response.CoffeeDrinkWithoutFavouriteResponse

class CoffeeDrinkResponseMapper {

    fun mapToCoffeeDrinkWithFavourite(
        coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithFavouriteResponse(
        coffeeDrink.id,
        coffeeDrink.name,
        coffeeDrink.imageUrl,
        coffeeDrink.description,
        coffeeDrink.ingredients,
        coffeeDrink.isFavourite
    )

    fun mapToCoffeeDrinkWithoutFavourite(
        coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithoutFavouriteResponse(
        coffeeDrink.id,
        coffeeDrink.name,
        coffeeDrink.imageUrl,
        coffeeDrink.description,
        coffeeDrink.ingredients
    )
}