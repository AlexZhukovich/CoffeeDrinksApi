package com.alexzh.coffeedrinks.api.users

import com.alexzh.coffeedrinks.addFormParamHeader
import com.alexzh.coffeedrinks.formItem
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.randomString
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.testEmail
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.launchMockAppWithRealMappers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import java.lang.RuntimeException
import kotlin.test.assertEquals

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class LoginUserApiTest {

    private val boundary = randomString()

    @Test
    fun `should return response with 500 code with missing email field error`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/login") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("password", "123456")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Missing email field", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code with missing password field error`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/login") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("email", "test@test.com")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Missing password field", response.content)
            }
        }
    }

    @Test
    fun `should return response with 400 code with retrieving user error`() {
        val email = testEmail()
        val password = "${randomString()}-incorrect}"
        val user = generateUser()

        launchMockAppWithRealMappers(
            hashFunction = { value: String -> value },
            beforeTest = {
                userRepository { stubGetUserByEmail(email, user) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/login") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("email", email),
                        formItem("password", password)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("Problem retrieving user", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code with retrieving user error`() {
        val email = testEmail()
        val password = randomString()

        launchMockAppWithRealMappers(
            hashFunction = { value: String -> value },
            beforeTest = {
                userRepository { stubGetUserByEmail(email, error = RuntimeException()) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/login") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("email", email),
                        formItem("password", password)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Problem retrieving user", response.content)
            }
        }
    }

    @Test
    fun `should return success response with jwt token`() {
        val user = generateUser()
        val token = randomString()

        launchMockAppWithRealMappers(
            hashFunction = { value: String -> value },
            beforeTest = {
                jwtService { stubGenerateToken(user, token) }
                userRepository { stubGetUserByEmail(user.email, user) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/login") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("email", user.email),
                        formItem("password", user.passwordHash)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(token, response.content)
            }
        }
    }
}