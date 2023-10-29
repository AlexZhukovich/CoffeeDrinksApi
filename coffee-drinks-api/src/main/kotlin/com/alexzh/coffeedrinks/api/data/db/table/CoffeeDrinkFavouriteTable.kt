package com.alexzh.coffeedrinks.api.data.db.table

import org.jetbrains.exposed.sql.Table

object CoffeeDrinkFavouriteTable : Table(name = "favourite_coffee_drink") {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references UsersTable.id
    val coffeeDrinkId = long("coffee_drink_id") references CoffeeDrinksTable.id
    val isFavourite = bool("favourite")

    override val primaryKey = PrimaryKey(id)
}