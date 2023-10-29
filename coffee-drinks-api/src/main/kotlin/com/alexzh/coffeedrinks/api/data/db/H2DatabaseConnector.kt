package com.alexzh.coffeedrinks.api.data.db

import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinkFavouriteTable
import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinksTable
import com.alexzh.coffeedrinks.api.data.db.table.UsersTable
import com.alexzh.coffeedrinks.api.data.predefinedCoffeeDrinks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class H2DatabaseConnector : DatabaseConnector {
    override fun connect(): Database {
        return Database.connect(hikari()).apply {
            transaction {
                SchemaUtils.create(
                    CoffeeDrinksTable,
                    UsersTable,
                    CoffeeDrinkFavouriteTable
                )

                predefinedCoffeeDrinks().forEach { dataToInsert ->
                    CoffeeDrinksTable.insert(dataToInsert)
                }
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        config.validate()
        return HikariDataSource(config)
    }
}