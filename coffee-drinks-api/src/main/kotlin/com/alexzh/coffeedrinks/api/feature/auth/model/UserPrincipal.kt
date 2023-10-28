package com.alexzh.coffeedrinks.api.feature.auth.model

import com.alexzh.coffeedrinks.api.data.model.User
import io.ktor.server.auth.*

class UserPrincipal(
    val user: User
): Principal
