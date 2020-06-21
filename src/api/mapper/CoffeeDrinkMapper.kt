package com.alexzh.coffeedrinks.api.api.mapper

import com.alexzh.coffeedrinks.api.api.model.CoffeeDrinkWithFavourite
import com.alexzh.coffeedrinks.api.api.model.CoffeeDrinkWithoutFavourite
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink

class CoffeeDrinkMapper {

    fun mapToCoffeeDrinkWithFavourite(
            coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithFavourite(
            coffeeDrink.id,
            coffeeDrink.name,
            coffeeDrink.imageUrl,
            coffeeDrink.description,
            coffeeDrink.ingredients,
            coffeeDrink.isFavourite
    )

    fun mapToCoffeeDrinkWithoutFavourite(
            coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithoutFavourite(
            coffeeDrink.id,
            coffeeDrink.name,
            coffeeDrink.imageUrl,
            coffeeDrink.description,
            coffeeDrink.ingredients
    )
}