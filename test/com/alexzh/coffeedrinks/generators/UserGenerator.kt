package com.alexzh.coffeedrinks.generators

import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.testEmail
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.randomLong
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.randomString
import com.alexzh.coffeedrinks.generators.RandomValuesGenerator.testUserName

object UserGenerator {

    fun generateUser(
            id: Long = randomLong(),
            name: String = testUserName(),
            email: String = testEmail(),
            passwordHash: String = randomString()
    ) = User(
        id,
        name,
        email,
        passwordHash
    )
}