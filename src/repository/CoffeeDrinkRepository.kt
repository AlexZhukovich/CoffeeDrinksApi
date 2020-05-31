package com.alexzh.coffeedrinks.api.repository

import com.alexzh.coffeedrinks.api.model.CoffeeDrink

interface CoffeeDrinkRepository {

    suspend fun getCoffeeDrinks(): List<CoffeeDrink>

    suspend fun getCoffeeDrinkById(id: Long): CoffeeDrink?
}