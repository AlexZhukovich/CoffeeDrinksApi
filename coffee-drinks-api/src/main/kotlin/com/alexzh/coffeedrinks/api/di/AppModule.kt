package com.alexzh.coffeedrinks.api.di

import com.alexzh.coffeedrinks.api.data.db.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.db.H2DatabaseConnector
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepositoryImpl
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.data.repository.UserRepositoryImpl
import com.alexzh.coffeedrinks.api.feature.auth.controller.AuthController
import com.alexzh.coffeedrinks.api.feature.auth.encryptor.Encryptor
import com.alexzh.coffeedrinks.api.feature.auth.encryptor.EncryptorImpl
import com.alexzh.coffeedrinks.api.feature.auth.service.JwtService
import com.alexzh.coffeedrinks.api.feature.auth.service.JwtServiceImpl
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("secret")) { System.getenv()["SECRET_KEY"] }

    single<Encryptor> {
        EncryptorImpl(secretKey = get(named("secret")))
    }

    single<JwtService> {
        JwtServiceImpl(secretKey = get(named("secret")))
    }

    single<DatabaseConnector> { H2DatabaseConnector() }
    factory<CoffeeDrinksRepository> { CoffeeDrinksRepositoryImpl() }
    factory<UserRepository> { UserRepositoryImpl() }

    factory { CoffeeDrinkResponseMapper() }

    factory {
        AuthController(
            encryptor = get(),
            jwtService = get(),
            userRepository = get()
        )
    }
}