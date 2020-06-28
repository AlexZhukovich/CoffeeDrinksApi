package com.alexzh.coffeedrinks.generators

import java.util.*
import kotlin.random.Random

object RandomValuesGenerator {

    fun randomLong() = Random.nextLong()

    fun randomString() = UUID.randomUUID().toString()

    fun randomBoolean() = Random.nextBoolean()

    fun testUserName() = "test"

    fun testEmail() = "test@test.com"
}