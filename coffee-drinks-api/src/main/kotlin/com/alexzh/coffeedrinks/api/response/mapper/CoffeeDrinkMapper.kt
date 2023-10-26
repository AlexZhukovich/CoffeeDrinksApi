package com.alexzh.coffeedrinks.api.response.mapper

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.response.CoffeeDrinkWithFavouriteResponse
import com.alexzh.coffeedrinks.api.response.CoffeeDrinkWithoutFavouriteResponse

class CoffeeDrinkMapper {

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