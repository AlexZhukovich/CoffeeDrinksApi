package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrinkFavouriteTable
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrinkTable
import org.jetbrains.exposed.sql.*

class MySQLCoffeeDrinkRepository : CoffeeDrinkRepository {

    override suspend fun getCoffeeDrinks(): List<CoffeeDrink> {
        return dbQuery {
            CoffeeDrinkTable.selectAll()
                    .map { toCoffeeDrink(it) }
        }
    }

    override suspend fun getCoffeeDrinksByUser(id: Long): List<CoffeeDrink> {
        val complexJoin = Join(
                table = CoffeeDrinkTable,
                otherTable = CoffeeDrinkFavouriteTable,
                joinType = JoinType.LEFT,
                onColumn = CoffeeDrinkTable.id,
                otherColumn = CoffeeDrinkFavouriteTable.coffeeDrinkId,
                additionalConstraint = { CoffeeDrinkFavouriteTable.userId eq id}
        )

        return dbQuery {
            complexJoin.selectAll()
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
        val favouriteValue = if (row.getOrNull(CoffeeDrinkFavouriteTable.isFavourite) == null) {
            false
        } else {
            row[CoffeeDrinkFavouriteTable.isFavourite] == "Y"
        }

        return CoffeeDrink(
                id = row[CoffeeDrinkTable.id],
                name = row[CoffeeDrinkTable.name],
                imageUrl = row[CoffeeDrinkTable.imageUrl],
                description = row[CoffeeDrinkTable.description],
                ingredients = row[CoffeeDrinkTable.ingredients],
                isFavourite = favouriteValue
        )
    }
}