package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.User

interface UserRepository {

    suspend fun createUser(user: User): User?

    suspend fun getUserById(id: Long): User?

    suspend fun getUserByEmail(email: String): User?

    // TODO: delete
    suspend fun getUsers(): List<User>
}