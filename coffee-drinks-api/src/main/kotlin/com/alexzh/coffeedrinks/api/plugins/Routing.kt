package com.alexzh.coffeedrinks.api.plugins

import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.feature.auth.controller.AuthController
import com.alexzh.coffeedrinks.api.feature.auth.route.authRoutes
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.route.coffeeDrinksRoutes
import com.alexzh.coffeedrinks.api.feature.unauthorized.route.unauthorizedRoute
import com.alexzh.coffeedrinks.api.feature.users.mapper.UserInfoResponseMapper
import com.alexzh.coffeedrinks.api.feature.users.route.usersRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    val authController: AuthController by inject()
    val coffeeDrinksRepository: CoffeeDrinksRepository by inject()
    val coffeeDrinkResponseMapper: CoffeeDrinkResponseMapper by inject()
    val userRepository: UserRepository by inject()
    val userResponseMapper: UserInfoResponseMapper by inject()

    install(Locations)

    routing {
        authRoutes(authController)
        coffeeDrinksRoutes(coffeeDrinksRepository, coffeeDrinkResponseMapper)
        usersRoute(userRepository, userResponseMapper)
        unauthorizedRoute()

        staticResources("/", "static")
    }
}
