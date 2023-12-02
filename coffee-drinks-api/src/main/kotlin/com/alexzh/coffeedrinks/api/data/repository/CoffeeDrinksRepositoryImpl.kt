package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinkFavoriteTable
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
            otherTable = CoffeeDrinkFavoriteTable,
            joinType = JoinType.LEFT,
            onColumn = CoffeeDrinksTable.id,
            otherColumn = CoffeeDrinkFavoriteTable.coffeeDrinkId,
            additionalConstraint = { CoffeeDrinkFavoriteTable.userId eq userId}
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
            otherTable = CoffeeDrinkFavoriteTable,
            joinType = JoinType.LEFT,
            onColumn = CoffeeDrinksTable.id,
            otherColumn = CoffeeDrinkFavoriteTable.coffeeDrinkId,
            additionalConstraint = {
                CoffeeDrinkFavoriteTable.userId eq userId
            }
        )

        return dbQuery {
            complexJoin.select { CoffeeDrinksTable.id eq coffeeDrinkById }
                .mapNotNull { toCoffeeDrink(it) }
                .singleOrNull()
        }
    }

    override suspend fun updateFavoriteStateOfCoffeeForUser(
        userId: Long,
        coffeeDrinkId: Long,
        isFavorite: Boolean
    ): Boolean {
        val coffeeDrink = getCoffeeDrinkByUserAndCoffeeDrinkId(userId, coffeeDrinkId)

        if (coffeeDrink?.isFavorite == isFavorite) {
            return false
        }

        var updated = false
        if (coffeeDrink != null) {
            if (coffeeDrink.isFavorite) {
                dbQuery {
                    updated = CoffeeDrinkFavoriteTable.deleteWhere {
                        CoffeeDrinkFavoriteTable.userId eq userId
                    } > 0
                }
            } else {
                dbQuery {
                    updated = CoffeeDrinkFavoriteTable.insert {
                        it[CoffeeDrinkFavoriteTable.userId] = userId
                        it[CoffeeDrinkFavoriteTable.coffeeDrinkId] = coffeeDrinkId
                        it[CoffeeDrinkFavoriteTable.isFavorite] = isFavorite
                    }.resultedValues?.first() != null
                }
            }
        }
        return updated
    }

    private fun toCoffeeDrink(row: ResultRow): CoffeeDrink {
        val favoriteValue = if (row.getOrNull(CoffeeDrinkFavoriteTable.isFavorite) == null) {
            false
        } else {
            row[CoffeeDrinkFavoriteTable.isFavorite]
        }

        return CoffeeDrink(
            id = row[CoffeeDrinksTable.id],
            name = row[CoffeeDrinksTable.name],
            imageUrl = row[CoffeeDrinksTable.imageUrl],
            description = row[CoffeeDrinksTable.description],
            ingredients = row[CoffeeDrinksTable.ingredients],
            isFavorite = favoriteValue
        )
    }
}