package com.alexzh.coffeedrinks.api.data.db

import org.jetbrains.exposed.sql.Database

interface DatabaseConnector {

    fun connect(): Database
}