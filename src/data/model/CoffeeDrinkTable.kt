package com.alexzh.coffeedrinks.api.data.model

import org.jetbrains.exposed.sql.Table

object CoffeeDrinkTable : Table(name = "coffee_drinks") {
    val id = long("id").autoIncrement().primaryKey()
    val name = varchar("title", 255)
    val imageUrl = varchar("photourl", 255)
    val description = text("description")
    val ingredients = varchar("ingredients", 256)
}