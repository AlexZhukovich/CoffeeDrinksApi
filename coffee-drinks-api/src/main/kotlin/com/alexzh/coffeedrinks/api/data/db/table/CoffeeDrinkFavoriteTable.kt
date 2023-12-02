package com.alexzh.coffeedrinks.api.data.db.table

import org.jetbrains.exposed.sql.Table

object CoffeeDrinkFavoriteTable : Table(name = "favorite_coffee_drink") {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references UsersTable.id
    val coffeeDrinkId = long("coffee_drink_id") references CoffeeDrinksTable.id
    val isFavorite = bool("favorite")

    override val primaryKey = PrimaryKey(id)
}