package com.alexzh.coffeedrinks.api.testframework.mockconfig.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import io.mockk.coEvery

class CoffeeDrinkRepositoryMockConfigurator(
        private val coffeeDrinkRepository: CoffeeDrinkRepository
) {
    fun stubGetCoffeeDrinks(
            coffeeDrinks: List<CoffeeDrink>
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinks() } returns coffeeDrinks
    }

    fun stubGetCoffeeDrinksById(
            userId: Long,
            coffeeDrinks: List<CoffeeDrink>
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinksByUser(userId) } returns coffeeDrinks
    }

    fun stubGetCoffeeDrinkById(
            id: Long,
            coffeeDrink: CoffeeDrink? = null
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinkById(id) } returns coffeeDrink
    }
}