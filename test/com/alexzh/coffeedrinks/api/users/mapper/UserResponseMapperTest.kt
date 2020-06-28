package com.alexzh.coffeedrinks.api.users.mapper

import com.alexzh.coffeedrinks.api.api.users.mapper.UserResponseMapper
import com.alexzh.coffeedrinks.api.api.users.model.UserResponse
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.generators.UserGenerator.generateUser
import org.junit.Test
import kotlin.test.assertEquals

class UserResponseMapperTest {

    private val mapper = UserResponseMapper()

    @Test
    fun `should map User to UserResponse`() {
        val user = generateUser()
        val userResponse = mapper.mapToResponse(user)

        assertUserResponse(user, userResponse)
    }

    private fun assertUserResponse(
        user: User,
        userResponse: UserResponse
    ) {
        assertEquals(user.id, userResponse.id)
        assertEquals(user.name, userResponse.name)
        assertEquals(user.email, userResponse.email)
    }
}