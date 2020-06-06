package com.alexzh.coffeedrinks.api.data.model

import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class User(
    val id: Long = -1,
    val name: String,
    val email: String,
    val passwordHash: String
) : Serializable, Principal

object UserTable : Table(name = "clients") {
    val id = long("id").autoIncrement().primaryKey()
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val passwordHash = varchar("password", 255)
}