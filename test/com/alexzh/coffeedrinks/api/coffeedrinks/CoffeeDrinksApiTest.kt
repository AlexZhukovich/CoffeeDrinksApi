package com.alexzh.coffeedrinks.api.coffeedrinks

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.addAuthHeader
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithFavouriteResponse
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithoutFavouriteResponse
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.generators.CoffeeDrinkGenerator.generateCoffeeDrink
import com.alexzh.coffeedrinks.generators.CoffeeDrinkGenerator.generateCoffeeDrinksByFavourites
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.launchMockAppWithRealMappers
import com.alexzh.coffeedrinks.testframework.*
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class CoffeeDrinksApiTest {
    private val mapper = CoffeeDrinkMapper()

    @Test
    fun `should return 2 coffee drinks when coffee-drinks request executed and no active user`() {
        val coffeeDrinks = generateCoffeeDrinksByFavourites(listOf(true, false))
        val expectedCoffeeDrinks = coffeeDrinks.map { mapper.mapToCoffeeDrinkWithoutFavourite(it) }

        launchMockAppWithRealMappers(
                beforeTest = {
                    coffeeDrinkRepository { stubGetCoffeeDrinks(coffeeDrinks) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedCoffeeDrinks, createJsonListOf(response.content))
            }
        }
    }

    @Test
    fun `should return 2 coffee drinks when coffee-drinks request executed with active user`() {
        val user = generateUser()
        val coffeeDrinks = generateCoffeeDrinksByFavourites(listOf(true, false))
        val expectedCoffeeDrinks = coffeeDrinks.map { mapper.mapToCoffeeDrinkWithFavourite(it) }

        launchMockAppWithRealMappers(
                beforeTest = {
                    jwtService { stubAuthJwtVerifier() }
                    userRepository { stubGetUserById(user.id, user) }
                    coffeeDrinkRepository { stubGetCoffeeDrinksById(user.id, coffeeDrinks) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks") {
                addAuthHeader(this@launchMockAppWithRealMappers, user)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedCoffeeDrinks, createJsonListOf(response.content))
            }
        }
    }

    @Test
    fun `should return empty list when database have no data executed`() {
        val coffeeDrinks = emptyList<CoffeeDrink>()
        val expectedCoffeeDrinks = emptyList<CoffeeDrinkWithoutFavouriteResponse>()

        launchMockAppWithRealMappers(
                beforeTest = {
                    coffeeDrinkRepository { stubGetCoffeeDrinks(coffeeDrinks) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedCoffeeDrinks, createJsonListOf(response.content))
            }
        }
    }

    @Test
    fun `should return coffee drink by id when no active user`() {
        val coffeeDrink = generateCoffeeDrink()
        val expectedCoffeeDrinkWithoutFavourite = mapper.mapToCoffeeDrinkWithoutFavourite(coffeeDrink)

        launchMockAppWithRealMappers(
                beforeTest = {
                    coffeeDrinkRepository { stubGetCoffeeDrinkById(coffeeDrink.id, coffeeDrink) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/${coffeeDrink.id}").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    expectedCoffeeDrinkWithoutFavourite,
                    createJsonObjectOf<CoffeeDrinkWithoutFavouriteResponse>(response.content)
                )
            }
        }
    }

    @Test
    fun `should return coffee drink by id with active user`() {
        val user = generateUser()
        val coffeeDrink = generateCoffeeDrink()
        val expectedCoffeeDrinkWithFavourite = mapper.mapToCoffeeDrinkWithFavourite(coffeeDrink)

        launchMockAppWithRealMappers(
                beforeTest = {
                    jwtService { stubAuthJwtVerifier() }
                    userRepository { stubGetUserById(user.id, user) }
                    coffeeDrinkRepository { stubGetCoffeeDrinkById(coffeeDrink.id, coffeeDrink) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/${coffeeDrink.id}") {
                addAuthHeader(this@launchMockAppWithRealMappers, user)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    expectedCoffeeDrinkWithFavourite,
                    createJsonObjectOf<CoffeeDrinkWithFavouriteResponse>(response.content)
                )
            }
        }
    }

    @Test
    fun `should return response with 404 code when coffee-drinks request with id = -1 is executed`() {
        val id = -1L

        launchMockAppWithRealMappers(
                beforeTest = {
                    coffeeDrinkRepository { stubGetCoffeeDrinkById(id, null) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/$id").apply {
                assertEquals(HttpStatusCode.NoContent, response.status())
                assertNull(response.content)
            }
        }
    }
}