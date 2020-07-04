package com.alexzh.coffeedrinks.api.api.users

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.api.AppSession
import com.alexzh.coffeedrinks.api.api.users.mapper.UserResponseMapper
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.data.exception.UserAlreadyExistException
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI

const val MIN_EMAIL_LENGTH = 4
const val MIN_USERNAME_LENGTH = 4
const val MIN_PASSWORD_LENGTH = 3

const val LOGIN_ENDPOINT = "$API_VERSION/login"
const val LOGOUT_ENDPOINT = "$API_VERSION/logout"
const val USERS_ENDPOINT = "$API_VERSION/users"
const val USER_BY_ID_ENDPOINT = "$USERS_ENDPOINT/{id}"

@KtorExperimentalLocationsAPI
object UsersApi {
    @Location(USERS_ENDPOINT)
    class CreateUser

    @Location(LOGIN_ENDPOINT)
    class LoginUser

    @Location(LOGOUT_ENDPOINT)
    class LogoutUser

    @Location(USER_BY_ID_ENDPOINT)
    class UserById(val id: Long)
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Route.users(
    userRepository: UserRepository,
    userResponseMapper: UserResponseMapper,
    jwtService: JwtService,
    hasFunction: (String) -> String
) {
    post<UsersApi.CreateUser> {
        val params = call.receive<Parameters>()
        val name = params["name"]
                ?: return@post call.respond(HttpStatusCode.InternalServerError, "Missing name field")
        val email = params["email"]
                ?: return@post call.respond(HttpStatusCode.InternalServerError, "Missing email field")
        val password = params["password"]
                ?: return@post call.respond(HttpStatusCode.InternalServerError, "Missing password field")
        val hashedPassword = hasFunction(password)

        when {
            email.length < MIN_EMAIL_LENGTH ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Email should be at least $MIN_EMAIL_LENGTH characters long"
                )
            !isEmailValid(email) ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Email value should be in [test]@[test].[test] format"
                )
            name.length < MIN_USERNAME_LENGTH ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Username should be at least $MIN_USERNAME_LENGTH characters long"
                )
            !isUserNameValid(name) ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Username should consist of digits, letters, dots or underscores"
                )
            password.length < MIN_PASSWORD_LENGTH ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Password should be at least $MIN_PASSWORD_LENGTH characters long"
                )
            else -> {
                try {
                    val user = userRepository.createUser(
                            User(name = name, email = email, passwordHash = hashedPassword)
                    )
                    user?.id?.let {
                        call.sessions.set(AppSession(it))
                        call.respondText(text = jwtService.generateToken(user), status = HttpStatusCode.Created)
                    }
                } catch (ex: UserAlreadyExistException) {
                    application.log.error("User already exist", ex)
                    call.respond(
                            HttpStatusCode.InternalServerError,
                            "User with the following username already exist"
                    )
                } catch (ex: Throwable) {
                    application.log.error("Failed to create a user", ex)
                    call.respond(
                            HttpStatusCode.InternalServerError,
                            "Failed to register user"
                    )
                }
            }
        }
    }
    post<UsersApi.LoginUser> {
        val params = call.receive<Parameters>()

        val email = params["email"]
                ?: return@post call.respond(HttpStatusCode.InternalServerError, "Missing email field")
        val password = params["password"]
                ?: return@post call.respond(HttpStatusCode.InternalServerError, "Missing password field")
        val hashedPassword = hasFunction(password)

        try {
            val user = userRepository.getUserByEmail(email)
            user?.id?.let {
                if (user.passwordHash == hashedPassword) {
                    call.sessions.set(AppSession(it))
                    call.respondText(jwtService.generateToken(user))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Problem retrieving user")
                }
            }
        } catch (ex: Throwable) {
            application.log.error("Problem retrieving user", ex)
            call.respond(HttpStatusCode.InternalServerError, "Problem retrieving user")
        }
    }
    authenticate("jwt", optional = true) {
        get<UsersApi.UserById> { userId ->
            val user = call.principal<User>()
            when {
                user == null -> {
                    call.respond(HttpStatusCode.BadRequest, "User should logged in")
                }
                user.id == userId.id -> {
                    call.respond(userResponseMapper.mapToResponse(user))
                }
                else -> {
                    call.respond(HttpStatusCode.BadRequest, "Problem retrieving active user information")
                }
            }
        }
        post<UsersApi.LogoutUser> {
            val user = call.principal<User>()
            if (user != null) {
                call.sessions.clear(call.sessions.findName(AppSession::class))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving user")
            }
        }
    }
}

private fun isUserNameValid(username: String): Boolean {
    val pattern = "[a-zA-Z0-9_.]+".toRegex()
    return username.matches(pattern)
}

private fun isEmailValid(email: String): Boolean {
    val pattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$".toRegex()
    return email.matches(pattern)
}