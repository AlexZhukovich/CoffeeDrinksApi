package com.alexzh.coffeedrinks.api.users

import com.alexzh.coffeedrinks.addAuthHeader
import com.alexzh.coffeedrinks.api.api.users.mapper.UserResponseMapper
import com.alexzh.coffeedrinks.api.api.users.model.UserResponse
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.randomLong
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.launchMockAppWithRealMappers
import com.alexzh.coffeedrinks.testframework.createJsonObjectOf
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class GetUserByIdApiTest {

    private val userMapper = UserResponseMapper()

    @Test
    fun `should return response with 400 code with User should be logged in error when user is no active`() {
        val id = randomLong()
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Get, "/api/v1/users/$id").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("User should logged in", response.content)
            }
        }
    }

    @Test
    fun `should return response with 400 code and Problem retrieving active user information error when user is active`() {
        val activeUser = generateUser()
        val id = randomLong() + activeUser.id
        launchMockAppWithRealMappers(
                beforeTest = {
                    jwtService { stubAuthJwtVerifier() }
                    userRepository { stubGetUserById(activeUser.id, activeUser) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/users/$id") {
                addAuthHeader(this@launchMockAppWithRealMappers, activeUser)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("Problem retrieving active user information", response.content)
            }
        }
    }

    @Test
    fun `should return success response with User data when user is active`() {
        val activeUser = generateUser()
        val expectedUserResponse = userMapper.mapToResponse(activeUser)
        launchMockAppWithRealMappers(
                beforeTest = {
                    jwtService { stubAuthJwtVerifier() }
                    userRepository { stubGetUserById(activeUser.id, activeUser) }
                }
        ) {
            handleRequest(HttpMethod.Get, "/api/v1/users/${activeUser.id}") {
                addAuthHeader(this@launchMockAppWithRealMappers, activeUser)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedUserResponse, createJsonObjectOf<UserResponse>(response.content))
            }
        }
    }
}