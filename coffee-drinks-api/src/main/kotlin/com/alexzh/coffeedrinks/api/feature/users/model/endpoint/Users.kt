package com.alexzh.coffeedrinks.api.feature.users.model.endpoint

import com.alexzh.coffeedrinks.api.common.BASE_URL
import io.ktor.server.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("${BASE_URL}/users")
class Users {

    @Location("/{id}")
    class UserInfo(val id: Long)
}