package com.alexzh.coffeedrinks.api.api

import com.alexzh.coffeedrinks.api.API_VERSION
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.redirect
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

const val USERS_ENDPOINT = "$API_VERSION/users"
const val USER_BY_ID_ENDPOINT = "$USERS_ENDPOINT/{id}"

@KtorExperimentalLocationsAPI
object UsersApi {
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
    get<UsersApi.AllUsers> {
        call.respond(
            userRepository.getUsers()
        )
    }
    post<UsersApi.AllUsers> {
        val params = call.receive<Parameters>()
        val name = params["name"] ?: return@post call.redirect(it)
        val email = params["email"] ?: return@post call.redirect(it)
        val password = params["password"] ?: return@post call.redirect(it)

        userRepository.createUser(
            User(
                name = name,
                email = email,
                passwordHash = password
            )
        )

        // TODO: return user info or error
        call.respondText { "User was created successfully" }
    }
    get<UsersApi.UserById> { userId ->
        val user = userRepository.getUserById(userId.id)
        if (user != null) {
            call.respond(user)
        } else {
            call.respond(HttpStatusCode.NoContent)
        }
    }
    // TODO: update user
    // TODO: delete user
}