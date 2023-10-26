package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink

interface CoffeeDrinksRepository {

    suspend fun getCoffeeDrinks(): List<CoffeeDrink>
}