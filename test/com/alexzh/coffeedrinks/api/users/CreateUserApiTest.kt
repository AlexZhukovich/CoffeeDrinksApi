package com.alexzh.coffeedrinks.api.users

import com.alexzh.coffeedrinks.addFormParamHeader
import com.alexzh.coffeedrinks.api.api.users.MIN_EMAIL_LENGTH
import com.alexzh.coffeedrinks.api.api.users.MIN_PASSWORD_LENGTH
import com.alexzh.coffeedrinks.api.api.users.MIN_USERNAME_LENGTH
import com.alexzh.coffeedrinks.api.data.exception.UserAlreadyExistException
import com.alexzh.coffeedrinks.formItem
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.randomString
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import com.alexzh.coffeedrinks.launchMockAppWithRealMappers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
class CreateUserApiTest {

    private val boundary = randomString()

    @Test
    fun `should return response with 500 code with missing name error`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("email", "test@test.test"),
                        formItem("password", "123456")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Missing name field", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code with missing email error`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                        boundary,
                        listOf(
                            formItem("name", "test"),
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
    fun `should return response with 500 code with missing password error`() {
        launchMockAppWithRealMappers {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                        boundary,
                        listOf(
                            formItem("email", "test@test.test"),
                            formItem("name", "test")
                        )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Missing password field", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and min email length error`() {
        launchMockAppWithRealMappers(
            hashFunction = {value -> value}
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", "test"),
                        formItem("email", "t"),
                        formItem("password", "123456")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Email should be at least $MIN_EMAIL_LENGTH characters long", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and invalid email error`() {
        launchMockAppWithRealMappers(
            hashFunction = {value -> value}
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", "test"),
                        formItem("email", "test"),
                        formItem("password", "123456")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Email value should be in [test]@[test].[test] format", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and min name length error`() {
        launchMockAppWithRealMappers(
            hashFunction = {value -> value}
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", ""),
                        formItem("email", "test@test.com"),
                        formItem("password", "123456")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Username should be at least $MIN_USERNAME_LENGTH characters long", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and invalid name error`() {
        launchMockAppWithRealMappers(
            hashFunction = {value -> value}
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                        boundary,
                        listOf(
                                formItem("name", "!>8}"),
                                formItem("email", "test@test.com"),
                                formItem("password", "123456")
                        )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Username should consist of digits, letters, dots or underscores", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and min password length error`() {
        launchMockAppWithRealMappers(
            hashFunction = {value -> value}
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", "test"),
                        formItem("email", "test@test.com"),
                        formItem("password", "")
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Password should be at least $MIN_PASSWORD_LENGTH characters long", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and user already exist error`() {
        val user = generateUser(id = -1L)

        launchMockAppWithRealMappers(
            hashFunction = { value -> value },
            beforeTest = {
                userRepository { stubCreateUser(user, UserAlreadyExistException()) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", user.name),
                        formItem("email", user.email),
                        formItem("password", user.passwordHash)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("User with the following username already exist", response.content)
            }
        }
    }

    @Test
    fun `should return response with 500 code and failed register user error`() {
        val user = generateUser(id = -1L)

        launchMockAppWithRealMappers(
            hashFunction = { value -> value },
            beforeTest = {
                userRepository { stubCreateUser(user, RuntimeException()) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", user.name),
                        formItem("email", user.email),
                        formItem("password", user.passwordHash)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Failed to register user", response.content)
            }
        }
    }

    @Test
    fun `should return success response with token`() {
        val user = generateUser(id = -1L)
        val token = randomString()

        launchMockAppWithRealMappers(
            hashFunction = { value -> value },
            beforeTest = {
                jwtService { stubGenerateToken(user, token) }
                userRepository { stubCreateUser(user) }
            }
        ) {
            handleRequest(HttpMethod.Post, "/api/v1/users") {
                addFormParamHeader(boundary)
                setBody(
                    boundary,
                    listOf(
                        formItem("name", user.name),
                        formItem("email", user.email),
                        formItem("password", user.passwordHash)
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals(token, response.content)
            }
        }
    }
}