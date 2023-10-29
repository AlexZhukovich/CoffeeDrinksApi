package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinkFavouriteTable
import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinksTable
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink
import com.alexzh.coffeedrinks.api.data.utils.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CoffeeDrinksRepositoryImpl : CoffeeDrinksRepository {

    override suspend fun getCoffeeDrinks(): List<CoffeeDrink> {
        return dbQuery {
            CoffeeDrinksTable.selectAll()
                .map { toCoffeeDrink(it) }
        }
    }

    override suspend fun getCoffeeDrinksByUser(userId: Long): List<CoffeeDrink> {
        val complexJoin = Join(
            table = CoffeeDrinksTable,
            otherTable = CoffeeDrinkFavouriteTable,
            joinType = JoinType.LEFT,
            onColumn = CoffeeDrinksTable.id,
            otherColumn = CoffeeDrinkFavouriteTable.coffeeDrinkId,
            additionalConstraint = { CoffeeDrinkFavouriteTable.userId eq userId}
        )

        return dbQuery {
            complexJoin.selectAll()
                .map { toCoffeeDrink(it) }
        }
    }

    override suspend fun getCoffeeDrinkById(id: Long): CoffeeDrink? {
        return dbQuery {
            CoffeeDrinksTable.select { CoffeeDrinksTable.id eq id }
                .mapNotNull { toCoffeeDrink(it) }
                .singleOrNull()
        }
    }

    override suspend fun getCoffeeDrinkByUserAndCoffeeDrinkId(
        userId: Long,
        coffeeDrinkById: Long
    ): CoffeeDrink? {
        val complexJoin = Join(
            table = CoffeeDrinksTable,
            otherTable = CoffeeDrinkFavouriteTable,
            joinType = JoinType.LEFT,
            onColumn = CoffeeDrinksTable.id,
            otherColumn = CoffeeDrinkFavouriteTable.coffeeDrinkId,
            additionalConstraint = {
                CoffeeDrinkFavouriteTable.userId eq userId
            }
        )

        return dbQuery {
            complexJoin.select { CoffeeDrinksTable.id eq coffeeDrinkById }
                .mapNotNull { toCoffeeDrink(it) }
                .singleOrNull()
        }
    }

    override suspend fun updateFavouriteStateOfCoffeeForUser(
        userId: Long,
        coffeeDrinkId: Long,
        isFavourite: Boolean
    ): Boolean {
        val coffeeDrink = getCoffeeDrinkByUserAndCoffeeDrinkId(userId, coffeeDrinkId)

        if (coffeeDrink?.isFavourite == isFavourite) {
            return false
        }

        var updated = false
        if (coffeeDrink != null) {
            if (coffeeDrink.isFavourite) {
                dbQuery {
                    updated = CoffeeDrinkFavouriteTable.deleteWhere {
                        CoffeeDrinkFavouriteTable.userId eq userId
                    } > 0
                }
            } else {
                dbQuery {
                    updated = CoffeeDrinkFavouriteTable.insert {
                        it[CoffeeDrinkFavouriteTable.userId] = userId
                        it[CoffeeDrinkFavouriteTable.coffeeDrinkId] = coffeeDrinkId
                        it[CoffeeDrinkFavouriteTable.isFavourite] = isFavourite
                    }.resultedValues?.first() != null
                }
            }
        }
        return updated
    }

    private fun toCoffeeDrink(row: ResultRow): CoffeeDrink {
        val favouriteValue = if (row.getOrNull(CoffeeDrinkFavouriteTable.isFavourite) == null) {
            false
        } else {
            row[CoffeeDrinkFavouriteTable.isFavourite]
        }

        return CoffeeDrink(
            id = row[CoffeeDrinksTable.id],
            name = row[CoffeeDrinksTable.name],
            imageUrl = row[CoffeeDrinksTable.imageUrl],
            description = row[CoffeeDrinksTable.description],
            ingredients = row[CoffeeDrinksTable.ingredients],
            isFavourite = favouriteValue
        )
    }
}