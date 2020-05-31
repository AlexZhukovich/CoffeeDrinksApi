package com.alexzh.coffeedrinks.api.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun <T> dbQuery(
    block: () -> T
) : T = withContext(Dispatchers.IO) {
    transaction { block() }
}