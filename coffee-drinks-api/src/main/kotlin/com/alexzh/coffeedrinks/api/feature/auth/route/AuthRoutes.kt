package com.alexzh.coffeedrinks.api.feature.auth.route

import com.alexzh.coffeedrinks.api.feature.auth.controller.AuthController
import com.alexzh.coffeedrinks.api.feature.auth.model.endpoint.UserAuth
import com.alexzh.coffeedrinks.api.feature.auth.model.param.CreateUserRequestParams
import com.alexzh.coffeedrinks.api.feature.auth.model.param.LoginRequestParams
import com.alexzh.coffeedrinks.api.feature.auth.model.response.AuthResponse
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.authRoutes(
    authController: AuthController
) {
    post<UserAuth.CreateUser> {
        val params = runCatching { call.receive<CreateUserRequestParams>() }.getOrElse {
            throw BadRequestException("The 'username', 'email' and 'password' parameters are required")
        }

        when (val response = authController.createUser(params)) {
            is AuthResponse.Created -> call.respond(response.status, response.token)
            is AuthResponse.Error -> call.respond(response.status, response.message)
            is AuthResponse.Success -> return@post
        }
    }
    post<UserAuth.LoginUser> {
        val params = runCatching { call.receive<LoginRequestParams>() }.getOrElse {
            throw BadRequestException("The 'email' and 'password' parameters are required")
        }

        when (val response = authController.login(params)) {
            is AuthResponse.Created -> return@post
            is AuthResponse.Success ->
                if (response.token != null) {
                    call.respond(response.status, response.token)
                } else call.respond(response.status)
            is AuthResponse.Error -> call.respond(response.status, response.message)
        }
    }
}