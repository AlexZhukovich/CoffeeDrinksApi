package com.alexzh.coffeedrinks.api.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.jdbc.Driver"
            jdbcUrl = System.getenv("JDBC_DATABASE_URL")
            username = System.getenv("COFFEE_DRINK_USERNAME")
            password = System.getenv("COFFEE_DRINK_PASSWORD")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ) : T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}