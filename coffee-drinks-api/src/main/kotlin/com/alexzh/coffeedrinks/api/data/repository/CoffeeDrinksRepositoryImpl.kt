package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinksTable
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class CoffeeDrinksRepositoryImpl : CoffeeDrinksRepository {

    override suspend fun getCoffeeDrinks(): List<CoffeeDrink> {
        return dbQuery {
            CoffeeDrinksTable.selectAll()
                .map { toCoffeeDrink(it) }
        }
    }

    // TODO: fix `isFavourite` because it always set false
    private fun toCoffeeDrink(row: ResultRow): CoffeeDrink {

        return CoffeeDrink(
            id = row[CoffeeDrinksTable.id],
            name = row[CoffeeDrinksTable.name],
            imageUrl = row[CoffeeDrinksTable.imageUrl],
            description = row[CoffeeDrinksTable.description],
            ingredients = row[CoffeeDrinksTable.ingredients],
            isFavourite = false
        )
    }
}