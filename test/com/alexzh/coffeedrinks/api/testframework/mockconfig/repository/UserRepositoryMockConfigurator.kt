package com.alexzh.coffeedrinks.api.testframework.mockconfig.repository

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
}