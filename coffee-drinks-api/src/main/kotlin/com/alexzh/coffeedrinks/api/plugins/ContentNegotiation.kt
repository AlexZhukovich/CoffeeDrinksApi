package com.alexzh.coffeedrinks.api.plugins

import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(
            json = Json {
                prettyPrint = true
            },
            contentType = Json
        )
    }
}