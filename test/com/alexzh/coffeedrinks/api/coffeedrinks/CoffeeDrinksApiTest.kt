package com.alexzh.coffeedrinks.api.coffeedrinks

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.addAuthHeader
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithoutFavourite
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.generators.CoffeeDrinkGenerator.generateCoffeeDrink
import com.alexzh.coffeedrinks.api.generators.CoffeeDrinkGenerator.generateCoffeeDrinksByFavourites
import com.alexzh.coffeedrinks.api.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.api.launchTestApp
import com.alexzh.coffeedrinks.api.stubAuthVerifier
import com.alexzh.coffeedrinks.api.testframework.*
import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class CoffeeDrinksApiTest {
    private val coffeeDrinkRepository = mockk<CoffeeDrinkRepository>()
    private val userRepository = mockk<UserRepository>()
    private val jwtService = mockk<JwtService>()

    private val mapper = CoffeeDrinkMapper()

    @Test
    fun `should return 2 coffee drinks when coffee-drinks request executed and no active user`() {
        val coffeeDrinks = generateCoffeeDrinksByFavourites(listOf(true, false))
        val expectedResponse = successResponse(coffeeDrinks.map { mapper.mapToCoffeeDrinkWithoutFavourite(it) })

        launchTestApp(
            coffeeDrinkRepository = coffeeDrinkRepository,
            coffeeDrinkMapper = mapper
        ) {
            stubGetCoffeeDrinks(coffeeDrinks)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertResponse(expectedResponse, response.toTestResponseOfList())
            }
        }
    }

    @Test
    fun `should return 2 coffee drinks when coffee-drinks request executed with active user`() {
        val user = generateUser()
        val coffeeDrinks = generateCoffeeDrinksByFavourites(listOf(true, false))
        val expectedResponse = successResponse(coffeeDrinks.map { mapper.mapToCoffeeDrinkWithFavourite(it) })

        stubAuthVerifier(jwtService)

        launchTestApp(
                coffeeDrinkRepository = coffeeDrinkRepository,
                coffeeDrinkMapper = mapper,
                userRepository = userRepository,
                jwtService = jwtService
        ) {
            stubGetCoffeeDrinksById(user.id, coffeeDrinks)
            stubGetUserById(user.id, user)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks") {
                addAuthHeader(this@launchTestApp, user)
            }.apply {
                assertResponse(expectedResponse, response.toTestResponseOfList())
            }
        }
    }

    @Test
    fun `should return empty list when database have no data executed`() {
        val coffeeDrinks = emptyList<CoffeeDrink>()
        val expectedResponse = successResponse(emptyList<CoffeeDrinkWithoutFavourite>())

        launchTestApp(
            coffeeDrinkRepository = coffeeDrinkRepository,
            coffeeDrinkMapper = mapper
        ) {
            stubGetCoffeeDrinks(coffeeDrinks)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks").apply {
                assertResponse(expectedResponse, response.toTestResponse())
            }
        }
    }

    @Test
    fun `should return coffee drink by id when no active user`() {
        val coffeeDrink = generateCoffeeDrink()
        val expectedCoffeeDrinkWithoutFavourite = mapper.mapToCoffeeDrinkWithoutFavourite(coffeeDrink)
        val expectedResponse = successResponse(expectedCoffeeDrinkWithoutFavourite)

        launchTestApp(
            coffeeDrinkRepository = coffeeDrinkRepository,
            coffeeDrinkMapper = mapper
        ) {
            stubGetCoffeeDrinkById(coffeeDrink.id, coffeeDrink)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/${coffeeDrink.id}").apply {
                assertResponse(
                    expectedResponse,
                    response.toTestResponse()
                )
            }
        }
    }

    @Test
    fun `should return coffee drink by id with active user`() {
        val user = generateUser()
        val coffeeDrink = generateCoffeeDrink()
        val expectedCoffeeDrinkWithFavourite = mapper.mapToCoffeeDrinkWithFavourite(coffeeDrink)
        val expectedResponse = successResponse(expectedCoffeeDrinkWithFavourite)

        stubAuthVerifier(jwtService)

        launchTestApp(
                coffeeDrinkRepository = coffeeDrinkRepository,
                coffeeDrinkMapper = mapper,
                jwtService = jwtService,
                userRepository = userRepository
        ) {
            stubGetUserById(user.id, user)
            stubGetCoffeeDrinkById(coffeeDrink.id, coffeeDrink)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/${coffeeDrink.id}") {
                addAuthHeader(this@launchTestApp, user)
            }.apply {
                assertResponse(
                    expectedResponse,
                    response.toTestResponse()
                )
            }
        }
    }

    @Test
    fun `should return response with 404 code when coffee-drinks request with id = -1 is executed`() {
        val id = -1L
        val expectedResponse = noContentResponse()

        launchTestApp(
            coffeeDrinkRepository = coffeeDrinkRepository,
            coffeeDrinkMapper = mapper
        ) {
            stubGetCoffeeDrinkById(id, null)

            handleRequest(HttpMethod.Get, "/api/v1/coffee-drinks/$id").apply {
                assertStatusCodeOfResponse(
                    expectedResponse,
                    response
                )
            }
        }
    }

    private fun stubGetCoffeeDrinks(
        coffeeDrinks: List<CoffeeDrink>
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinks() } returns coffeeDrinks
    }

    private fun stubGetCoffeeDrinksById(
        userId: Long,
        coffeeDrinks: List<CoffeeDrink>
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinksByUser(userId) } returns coffeeDrinks
    }

    private fun stubGetUserById(
        userId: Long,
        user: User
    ) {
        coEvery { userRepository.getUserById(userId) } returns user
    }

    private fun stubGetCoffeeDrinkById(
        id: Long,
        coffeeDrink: CoffeeDrink? = null
    ) {
        coEvery { coffeeDrinkRepository.getCoffeeDrinkById(id) } returns coffeeDrink
    }
}