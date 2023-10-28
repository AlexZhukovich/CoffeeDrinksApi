package com.alexzh.coffeedrinks.api.data.db.table

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", length = 45)
    val email = varchar("email", length = 45)
    val password = text("password")

    override val primaryKey =  PrimaryKey(CoffeeDrinksTable.id, name = "PK_User_ID")
}