package com.alexzh.coffeedrinks.testframework.mockconfig

import api.coffeedrinks.mapper.CoffeeDrinkMapper
import com.alexzh.coffeedrinks.api.api.users.mapper.UserResponseMapper
import com.alexzh.coffeedrinks.api.auth.JwtService
import com.alexzh.coffeedrinks.api.data.database.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinkRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.testframework.mockconfig.repository.CoffeeDrinkRepositoryMockConfigurator
import com.alexzh.coffeedrinks.testframework.mockconfig.repository.UserRepositoryMockConfigurator
import com.alexzh.coffeedrinks.testframework.mockconfig.service.JwtServiceMockConfigurator
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
class MockConfigurator(
        val databaseConnector: DatabaseConnector,
        val coffeeDrinkRepository: CoffeeDrinkRepository,
        val coffeeDrinkMapper: CoffeeDrinkMapper,
        val userRepository: UserRepository,
        val userMapper: UserResponseMapper,
        val jwtService: JwtService,
        val hashFunction: (String) -> String,
        private val jwtIssuer: String,
        private val jwtSecret: String
) {
    fun jwtService(
            jwtService: JwtService = this.jwtService,
            func: JwtServiceMockConfigurator.() -> Unit
    ) = JwtServiceMockConfigurator(jwtIssuer, jwtSecret, jwtService).apply(func)

    fun userRepository(
            userRepository: UserRepository = this.userRepository,
            func: UserRepositoryMockConfigurator.() -> Unit
    ) = UserRepositoryMockConfigurator(userRepository).apply(func)

    fun coffeeDrinkRepository(
            coffeeDrinkRepository: CoffeeDrinkRepository = this.coffeeDrinkRepository,
            func: CoffeeDrinkRepositoryMockConfigurator.() -> Unit
    ) = CoffeeDrinkRepositoryMockConfigurator(coffeeDrinkRepository).apply(func)
}