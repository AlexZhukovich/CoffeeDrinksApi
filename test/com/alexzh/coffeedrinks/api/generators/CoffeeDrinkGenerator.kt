package com.alexzh.coffeedrinks.api.generators

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomBoolean
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomLong
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomString

object CoffeeDrinkGenerator {

    fun generateCoffeeDrink(
        id: Long = randomLong(),
        name: String = randomString(),
        imageUrl: String = randomString(),
        description: String = randomString(),
        ingredients: String = randomString(),
        isFavourite: Boolean = randomBoolean()
    ) = CoffeeDrink(
        id,
        name,
        imageUrl,
        description,
        ingredients,
        isFavourite
    )

    fun generateCoffeeDrinksByFavourites(
        favouriteList: List<Boolean>
    ) : List<CoffeeDrink> {
        return favouriteList.map {
            generateCoffeeDrink(isFavourite = it)
        }
    }
}