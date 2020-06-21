package com.alexzh.coffeedrinks.api.data.model

import org.jetbrains.exposed.sql.Table

object CoffeeDrinkFavouriteTable : Table(name = "favourite_coffee_drink") {
    val id = CoffeeDrinkTable.long("id").autoIncrement().primaryKey()
    val userId = long("userid").references(UserTable.id)
    val coffeeDrinkId = long("coffeeid").references(CoffeeDrinkTable.id)
    val isFavourite = varchar("favourite", 1)
}