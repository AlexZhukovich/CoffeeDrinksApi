package com.alexzh.coffeedrinks.api.feature.unauthorized.route

import com.alexzh.coffeedrinks.api.feature.unauthorized.model.endpoint.Unauthorized
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.unauthorizedRoute() {
    get<Unauthorized> {
        call.respond(
            message = "Not Authorized",
            status = HttpStatusCode.Unauthorized
        )
    }
}