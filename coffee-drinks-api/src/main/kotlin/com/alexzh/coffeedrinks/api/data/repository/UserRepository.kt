package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.User

interface UserRepository {

    suspend fun createUser(user: User): User?

    suspend fun getUserById(userId: Long): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserByEmailAndPassword(
        email: String,
        password: String
    ): User?
}