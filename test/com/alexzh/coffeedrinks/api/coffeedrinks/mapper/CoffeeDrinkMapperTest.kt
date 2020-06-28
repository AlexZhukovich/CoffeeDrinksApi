package com.alexzh.coffeedrinks.api.coffeedrinks.mapper

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithFavouriteResponse
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithoutFavouriteResponse
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.generators.CoffeeDrinkGenerator.generateCoffeeDrink
import org.junit.Test
import kotlin.test.assertEquals

class CoffeeDrinkMapperTest {

    private val mapper = CoffeeDrinkMapper()

    @Test
    fun `should map CoffeeDrink to CoffeeDrinkWithFavourite when favourite is true`() {
        val coffeeDrink = generateCoffeeDrink(isFavourite = true)
        val result = mapper.mapToCoffeeDrinkWithFavourite(coffeeDrink)
        assertCoffeeDrinks(coffeeDrink, result)
    }

    @Test
    fun `should map CoffeeDrink to CoffeeDrinkWithFavourite when favourite is false`() {
        val coffeeDrink = generateCoffeeDrink(isFavourite = false)
        val result = mapper.mapToCoffeeDrinkWithFavourite(coffeeDrink)
        assertCoffeeDrinks(coffeeDrink, result)
    }

    @Test
    fun `should map CoffeeDrink to CoffeeDrinkWithoutFavourite`() {
        val coffeeDrink = generateCoffeeDrink()
        val result = mapper.mapToCoffeeDrinkWithoutFavourite(coffeeDrink)
        assertCoffeeDrinks(coffeeDrink, result)
    }

    private fun assertCoffeeDrinks(
            coffeeDrink: CoffeeDrink,
            coffeeDrinkWithFavouriteResponse: CoffeeDrinkWithFavouriteResponse
    ) {
        assertEquals(coffeeDrink.id, coffeeDrinkWithFavouriteResponse.id)
        assertEquals(coffeeDrink.name, coffeeDrinkWithFavouriteResponse.name)
        assertEquals(coffeeDrink.imageUrl, coffeeDrinkWithFavouriteResponse.imageUrl)
        assertEquals(coffeeDrink.description, coffeeDrinkWithFavouriteResponse.description)
        assertEquals(coffeeDrink.ingredients, coffeeDrinkWithFavouriteResponse.ingredients)
        assertEquals(coffeeDrink.isFavourite, coffeeDrinkWithFavouriteResponse.isFavourite)
    }

    private fun assertCoffeeDrinks(
            coffeeDrink: CoffeeDrink,
            coffeeDrinkWithoutFavouriteResponse: CoffeeDrinkWithoutFavouriteResponse
    ) {
        assertEquals(coffeeDrink.id, coffeeDrinkWithoutFavouriteResponse.id)
        assertEquals(coffeeDrink.name, coffeeDrinkWithoutFavouriteResponse.name)
        assertEquals(coffeeDrink.imageUrl, coffeeDrinkWithoutFavouriteResponse.imageUrl)
        assertEquals(coffeeDrink.description, coffeeDrinkWithoutFavouriteResponse.description)
        assertEquals(coffeeDrink.ingredients, coffeeDrinkWithoutFavouriteResponse.ingredients)
    }
}