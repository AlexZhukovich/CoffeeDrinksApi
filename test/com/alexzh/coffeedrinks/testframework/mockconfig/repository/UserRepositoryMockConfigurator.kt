package com.alexzh.coffeedrinks.testframework.mockconfig.repository

import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import io.mockk.coEvery

class UserRepositoryMockConfigurator(
    private val userRepository: UserRepository
) {
    fun stubGetUserById(
            userId: Long,
            user: User
    ) {
        coEvery { userRepository.getUserById(userId) } returns user
    }

    fun stubCreateUser(
        user: User,
        error: Throwable? = null
    ) {
        if (error != null) {
            coEvery { userRepository.createUser(eq(user)) } throws error
        } else {
            coEvery { userRepository.createUser(eq(user)) } returns user
        }
    }

    fun stubGetUserByEmail(
        email: String,
        user: User? = null,
        error: Throwable? = null
    ) {
        if (error != null) {
            coEvery { userRepository.getUserByEmail(email) } throws error
        } else {
            coEvery { userRepository.getUserByEmail(email) } returns user
        }
    }
}