package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun deleteUser(id: Long): Boolean

    suspend fun updateUser(user: User): Boolean

    suspend fun getUserById(id: Long): User?

    suspend fun getUsers(): List<User>
}