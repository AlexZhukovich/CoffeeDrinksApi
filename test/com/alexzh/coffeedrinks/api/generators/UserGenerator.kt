package com.alexzh.coffeedrinks.api.generators

import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomEmail
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomLong
import com.alexzh.coffeedrinks.api.generators.RandomValuesGenerator.randomString

object UserGenerator {

    fun generateUser(
        id: Long = randomLong(),
        name: String = randomString(),
        email: String = randomEmail(),
        passwordHash: String = randomString()
    ) = User(
        id,
        name,
        email,
        passwordHash
    )
}