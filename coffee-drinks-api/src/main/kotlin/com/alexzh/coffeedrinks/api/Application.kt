package com.alexzh.coffeedrinks.api

import com.alexzh.coffeedrinks.api.data.db.DatabaseConnector
import com.alexzh.coffeedrinks.api.plugins.*
import com.alexzh.coffeedrinks.api.plugins.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.java.KoinJavaComponent.get

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureStatusPages()
    configureSecurity()
    configureHTTP()
    configureRouting()
    configureContentNegotiation()

    val databaseConnector: DatabaseConnector = get(DatabaseConnector::class.java)
    databaseConnector.connect()
}
