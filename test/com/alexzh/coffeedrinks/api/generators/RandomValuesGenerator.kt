package com.alexzh.coffeedrinks.api.generators

import java.util.*
import kotlin.random.Random

object RandomValuesGenerator {

    fun randomLong() = Random.nextLong()

    fun randomEmail() = "${randomString()}@test.com"

    fun randomString() = UUID.randomUUID().toString()

    fun randomBoolean() = Random.nextBoolean()
}