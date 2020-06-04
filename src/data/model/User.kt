package com.alexzh.coffeedrinks.api.data.model

import org.jetbrains.exposed.sql.Table

data class User(
    val id: Long = -1,
    val name: String,
    val email: String,
    val passwordHash: String
)

object UserTable : Table(name = "clients") {
    val id = long("id").primaryKey()
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val passwordHash = varchar("password", 255)
}