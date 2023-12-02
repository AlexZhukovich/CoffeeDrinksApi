package com.alexzh.coffeedrinks.api.feature.auth.model.endpoint

import com.alexzh.coffeedrinks.api.common.BASE_URL
import io.ktor.server.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("${BASE_URL}/users")
class UserAuth {

    @Location("/create")
    class CreateUser()

    @Location("/login")
    class LoginUser()
}