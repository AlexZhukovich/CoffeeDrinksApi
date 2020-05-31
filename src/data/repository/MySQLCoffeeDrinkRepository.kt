package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrinkTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class MySQLCoffeeDrinkRepository : CoffeeDrinkRepository {

    override suspend fun getCoffeeDrinks(): List<CoffeeDrink> {
        return dbQuery {
            CoffeeDrinkTable.selectAll()
                    .map { toCoffeeDrink(it) }
        }
    }

    override suspend fun getCoffeeDrinkById(id: Long): CoffeeDrink? {
        return dbQuery {
            CoffeeDrinkTable.select { CoffeeDrinkTable.id eq id }
                    .mapNotNull { toCoffeeDrink(it) }
                    .singleOrNull()
        }
    }

    private fun toCoffeeDrink(row: ResultRow): CoffeeDrink {
        return CoffeeDrink(
                id = row[CoffeeDrinkTable.id],
                name = row[CoffeeDrinkTable.name],
                imageUrl = row[CoffeeDrinkTable.imageUrl],
                description = row[CoffeeDrinkTable.description],
                ingredients = row[CoffeeDrinkTable.ingredients]
        )
    }
}