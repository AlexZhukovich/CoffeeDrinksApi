package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink

interface CoffeeDrinksRepository {

    suspend fun getCoffeeDrinks(): List<CoffeeDrink>

    suspend fun getCoffeeDrinksByUser(userId: Long): List<CoffeeDrink>

    suspend fun getCoffeeDrinkById(coffeeDrinkId: Long): CoffeeDrink?

    suspend fun getCoffeeDrinkByUserAndCoffeeDrinkId(
        userId: Long,
        coffeeDrinkById: Long
    ): CoffeeDrink?

    suspend fun updateFavouriteStateOfCoffeeForUser(
        userId: Long,
        coffeeDrinkId: Long,
        isFavourite: Boolean
    ): Boolean
}