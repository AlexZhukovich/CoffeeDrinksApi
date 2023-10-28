package com.alexzh.coffeedrinks.api.di

import com.alexzh.coffeedrinks.api.data.db.DatabaseConnector
import com.alexzh.coffeedrinks.api.data.db.H2DatabaseConnector
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepository
import com.alexzh.coffeedrinks.api.data.repository.CoffeeDrinksRepositoryImpl
import com.alexzh.coffeedrinks.api.feature.coffeedrinks.mapper.CoffeeDrinkResponseMapper
import org.koin.dsl.module

val appModule = module {
    single<DatabaseConnector> { H2DatabaseConnector() }
    factory<CoffeeDrinksRepository> { CoffeeDrinksRepositoryImpl() }

    factory { CoffeeDrinkResponseMapper() }
}