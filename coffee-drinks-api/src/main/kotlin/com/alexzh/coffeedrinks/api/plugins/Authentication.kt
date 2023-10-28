package com.alexzh.coffeedrinks.api.plugins

import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.feature.auth.model.UserPrincipal
import com.alexzh.coffeedrinks.api.feature.auth.service.JwtService
import com.alexzh.coffeedrinks.api.feature.unauthorized.model.endpoint.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureAuthentication() {
    val jwtService: JwtService by inject()
    val userRepository: UserRepository by inject()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            validate {
                val userId = it.payload.getClaim(jwtService.claim).asLong()

                val user = userRepository.getUserById(userId)
                if (user != null) UserPrincipal(user) else null
            }
            challenge { _, _ ->
                call.respondRedirect {
                    call.respondRedirect(
                        this@configureAuthentication.locations.href(Unauthorized())
                    )
                }
            }
        }
    }
}