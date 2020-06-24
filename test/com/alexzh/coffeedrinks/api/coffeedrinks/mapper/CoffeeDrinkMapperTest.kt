package com.alexzh.coffeedrinks.api.coffeedrinks.mapper

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithFavourite
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithoutFavourite
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.generators.CoffeeDrinkGenerator.generateCoffeeDrink
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
        coffeeDrinkWithFavourite: CoffeeDrinkWithFavourite
    ) {
        assertEquals(coffeeDrink.id, coffeeDrinkWithFavourite.id)
        assertEquals(coffeeDrink.name, coffeeDrinkWithFavourite.name)
        assertEquals(coffeeDrink.imageUrl, coffeeDrinkWithFavourite.imageUrl)
        assertEquals(coffeeDrink.description, coffeeDrinkWithFavourite.description)
        assertEquals(coffeeDrink.ingredients, coffeeDrinkWithFavourite.ingredients)
        assertEquals(coffeeDrink.isFavourite, coffeeDrinkWithFavourite.isFavourite)
    }

    private fun assertCoffeeDrinks(
        coffeeDrink: CoffeeDrink,
        coffeeDrinkWithoutFavourite: CoffeeDrinkWithoutFavourite
    ) {
        assertEquals(coffeeDrink.id, coffeeDrinkWithoutFavourite.id)
        assertEquals(coffeeDrink.name, coffeeDrinkWithoutFavourite.name)
        assertEquals(coffeeDrink.imageUrl, coffeeDrinkWithoutFavourite.imageUrl)
        assertEquals(coffeeDrink.description, coffeeDrinkWithoutFavourite.description)
        assertEquals(coffeeDrink.ingredients, coffeeDrinkWithoutFavourite.ingredients)
    }
}