package com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
class CoffeeDrinksApiTest {

    private val databaseConnector = mockk<DatabaseConnector>(relaxed = true)
    private val coffeeDrinkRepository = mockk<CoffeeDrinkRepository>()

    @Test
    fun `should return 2 coffee drinks when coffee-drinks request executed`() {
        val coffeeDrinks = listOf(
            CoffeeDrink(1L, "Coffee 1", "-", "Description 1", "Ingredients 1"),
            CoffeeDrink(2L, "Coffee 2", "no", "Description 2", "Ingredients 2")
        )
        stubGetCoffeeDrinks(coffeeDrinks)

        withTestApplication({ moduleWithDependencies(databaseConnector, coffeeDrinkRepository) }) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(coffeeDrinks, Gson().fromJson(response.content, object : TypeToken<List<CoffeeDrink>>() {}.type) )
            }
        }
    }

    @Test
    fun `should return empty list when database have no data executed`() {
        val coffeeDrinks = emptyList<CoffeeDrink>()
        stubGetCoffeeDrinks(coffeeDrinks)

        withTestApplication({ moduleWithDependencies(databaseConnector, coffeeDrinkRepository) }) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(coffeeDrinks, Gson().fromJson(response.content, object : TypeToken<List<CoffeeDrink>>() {}.type) )
            }
        }
    }

    @Test
    fun `should return coffee drink by id when coffee-drinks request with id = 42 is executed`() {
        val id = 42L
        val coffeeDrink = CoffeeDrink(1L, "Coffee 1", "-", "Description 1", "Ingredients 1")
        stubGetCoffeeDrinkById(id, coffeeDrink)

        withTestApplication({ moduleWithDependencies(databaseConnector, coffeeDrinkRepository) }) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/$id").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(coffeeDrink, Gson().fromJson(response.content, CoffeeDrink::class.java) )
            }
        }
    }

    @Test
    fun `should return response with 404 code when coffee-drinks request with id = -1 is executed`() {
        val id = -1L
        stubGetCoffeeDrinkById(id, null)

        withTestApplication({ moduleWithDependencies(databaseConnector, coffeeDrinkRepository) }) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/$id").apply {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    private fun stubGetCoffeeDrinks(
        coffeeDrinks: List<CoffeeDrink>
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinks() }.returns(coffeeDrinks)
    }

    private fun stubGetCoffeeDrinkById(
        id: Long,
        coffeeDrink: CoffeeDrink? = null
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinkById(id) }.returns(coffeeDrink)
    }
}