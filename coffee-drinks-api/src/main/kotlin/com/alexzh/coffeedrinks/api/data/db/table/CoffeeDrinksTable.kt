package com.alexzh.coffeedrinks.api.data.db.table

import org.jetbrains.exposed.sql.Table

object CoffeeDrinksTable : Table(name = "coffee_drinks") {
    val id = long("id").autoIncrement()
    val name = varchar("title", 255)
    val imageUrl = varchar("image_url", 255)
    val description = text("description")
    val ingredients = varchar("ingredients", 256)

    override val primaryKey =  PrimaryKey(id)
}
