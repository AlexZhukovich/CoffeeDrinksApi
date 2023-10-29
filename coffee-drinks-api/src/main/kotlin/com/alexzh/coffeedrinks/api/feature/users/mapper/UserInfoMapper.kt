package com.alexzh.coffeedrinks.api.feature.users.mapper

import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.feature.users.model.response.UserInfoResponse

class UserInfoResponseMapper {

    fun mapToUserResponse(
        user: User
    ) = UserInfoResponse(
        user.id,
        user.name,
        user.email
    )
}