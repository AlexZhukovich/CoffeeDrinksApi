package com.alexzh.coffeedrinks.api.feature.users.route

import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.feature.auth.model.UserPrincipal
import com.alexzh.coffeedrinks.api.feature.unauthorized.model.endpoint.Unauthorized
import com.alexzh.coffeedrinks.api.feature.users.mapper.UserInfoResponseMapper
import com.alexzh.coffeedrinks.api.feature.users.model.endpoint.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.usersRoute(
    userRepository: UserRepository,
    mapper: UserInfoResponseMapper
) {
    authenticate("jwt") {
        get<Users.UserInfo> {
            val user = call.principal<UserPrincipal>()
            if (user != null) {
                if (user.user.id == it.id) {
                    val foundUser = userRepository.getUserById(it.id);
                    if (foundUser != null) {
                        call.respond(
                            mapper.mapToUserResponse(foundUser)
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(locations.href(Unauthorized::class.java))
            }
        }
    }
}