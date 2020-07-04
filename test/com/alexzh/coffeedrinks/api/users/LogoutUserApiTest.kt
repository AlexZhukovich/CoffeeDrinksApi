package com.alexzh.coffeedrinks.api.users

import com.alexzh.coffeedrinks.addAuthHeader
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.launchMockAppWithRealMappers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class LogoutUserApiTest {

    @Test
    fun `should return response with 400 code and Problems retrieving user error when user is no active`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/logout").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("Problems retrieving user", response.content)
            }
        }
    }

    @Test
    fun `should return success response when user is active`() {
        val user = generateUser()
        launchMockAppWithRealMappers(
            beforeTest = {
                jwtService { stubAuthJwtVerifier() }
                userRepository { stubGetUserById(user.id, user) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/logout") {
                addAuthHeader(this@launchMockAppWithRealMappers, user)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}