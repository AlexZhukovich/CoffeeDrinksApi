package com.alexzh.coffeedrinks.api.repository

import com.alexzh.coffeedrinks.api.model.CoffeeDrink

class RuntimeCoffeeDrinkRepository : CoffeeDrinkRepository {
    private val coffeeDrinks: List<CoffeeDrink> = initCoffeeDrinks()

    override suspend fun getCoffeeDrinks(): List<CoffeeDrink> {
        return coffeeDrinks
    }

    override suspend fun getCoffeeDrinkById(id: Long): CoffeeDrink? {
        return coffeeDrinks.first { it.id == id }
    }

    private fun initCoffeeDrinks(): List<CoffeeDrink> {
        return mutableListOf(
            CoffeeDrink(
                id = 1L,
                name = "Americano",
                imageUrl = "https://api.coffee-drinks.alexzh.com/store/img/512/americano.png",
                description = "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee.",
                ingredients = "Espresso, Water"
            ),
            CoffeeDrink(
                id = 2L,
                name = "Cappuccino",
                imageUrl = "https://api.coffee-drinks.alexzh.com/store/img/512/cappuccino.png",
                description = "A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with steamed milk foam.",
                ingredients = "Espresso, Steamed milk foam"
            ),
            CoffeeDrink(
                id = 3L,
                name = "Latte Macchiato",
                imageUrl = "https://api.coffee-drinks.alexzh.com/store/img/512/latte-macchiato.png",
                description = "Latte Macchiato is a coffee beverage; the name literally means stained milk.",
                ingredients = "Espresso, Milk, Milk foam, Flavoured coffee syrup"
            )
        )
    }
}