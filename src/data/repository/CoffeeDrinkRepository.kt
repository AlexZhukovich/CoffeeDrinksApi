package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink

interface CoffeeDrinkRepository {

    suspend fun getCoffeeDrinks(): List<CoffeeDrink>

    suspend fun getCoffeeDrinksByUser(id: Long): List<CoffeeDrink>

    suspend fun getCoffeeDrinkById(id: Long): CoffeeDrink?
}