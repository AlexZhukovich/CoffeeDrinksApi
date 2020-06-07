package com.alexzh.coffeedrinks.api.api

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.api.response.UserResponse
import com.alexzh.coffeedrinks.api.auth.JwtService
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

    @Location(USERS_ENDPOINT)
    class AllUsers

    @Location(USER_BY_ID_ENDPOINT)
    class UserById(val id: Long)
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Route.users(
    userRepository: UserRepository,
    jwtService: JwtService,
    hasFunction: (String) -> String
) {
    post<UsersApi.CreateUser> {
        val params = call.receive<Parameters>()
        val name = params["name"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing fields")
        val email = params["email"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing fields")
        val password = params["password"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing fields")
        val hashedPassword = hasFunction(password)

        try {
            val user = userRepository.createUser(
                    User(name = name, email = email, passwordHash = hashedPassword)
            )
            user?.id?.let {
                call.sessions.set(AppSession(it))
                call.respondText(text = jwtService.generateToken(user), status = HttpStatusCode.Created)
            }
        } catch (ex: Throwable) {
            application.log.error("Failed to create a user", ex)
            call.respond(HttpStatusCode.BadRequest, "Problem with creating user")
        }
    }
    post<UsersApi.LoginUser> {
        val params = call.receive<Parameters>()

        val email = params["email"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing fields")
        val password = params["password"]
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing fields")
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
            application.log.error("Failed to register user", ex)
            call.respond(HttpStatusCode.BadRequest, "Problem retrieving user")
        }
    }
    authenticate("jwt", optional = true) {
        get<UsersApi.UserById> { userId ->
            val user = userRepository.getUserById(userId.id)
            if (user != null) {
                call.respond(UserResponse(user.id, user.name, user.email))
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
        // TODO: delete UsersApi.AllUsers and get request
        get<UsersApi.AllUsers> {
            val user = call.principal<User>()

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving user")
                return@get
            } else {
                try {
                    call.respond(
                            userRepository.getUsers()
                                    .map { UserResponse(it.id, it.name, it.email) }
                    )
                } catch (ex: Throwable) {
                    application.log.error("Failed to load all users", ex)
                    call.respond(HttpStatusCode.BadRequest, "Problems retrieving user")
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