package com.alexzh.coffeedrinks.api.data.model

import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class CoffeeDrink(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String
) : Serializable

object CoffeeDrinkTable : Table(name = "coffee_drinks") {
    val id = long("id").primaryKey()
    val name = varchar("title", 255)
    val imageUrl = varchar("photourl", 255)
    val description = text("description")
    val ingredients = varchar("ingredients", 256)
}