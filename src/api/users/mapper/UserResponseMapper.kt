package com.alexzh.coffeedrinks.api.api.users.mapper

import com.alexzh.coffeedrinks.api.api.users.model.UserResponse
import com.alexzh.coffeedrinks.api.data.model.User

class UserResponseMapper {

    fun mapToResponse(
        user: User
    ) = UserResponse(
        user.id,
        user.name,
        user.email
    )
}