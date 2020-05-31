package com.alexzh.coffeedrinks.api.data.database

import org.jetbrains.exposed.sql.Database

interface DatabaseConnector {

    fun connect(): Database
}