package com.alexzh.coffeedrinks.api.feature.auth.controller

import com.alexzh.coffeedrinks.api.data.exception.UserAlreadyExistException
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.repository.UserRepository
import com.alexzh.coffeedrinks.api.feature.auth.encryptor.Encryptor
import com.alexzh.coffeedrinks.api.feature.auth.model.response.AuthResponse
import com.alexzh.coffeedrinks.api.feature.auth.service.JwtService
import com.alexzh.coffeedrinks.api.feature.auth.model.param.CreateUserRequestParams
import com.alexzh.coffeedrinks.api.feature.auth.model.param.LoginRequestParams

class AuthController(
    private val encryptor: Encryptor,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {

    companion object {
        const val MIN_EMAIL_LENGTH = 4
        const val MIN_USERNAME_LENGTH = 4
        const val MIN_PASSWORD_LENGTH = 4
    }

    suspend fun createUser(
        params: CreateUserRequestParams
    ): AuthResponse {
        return when {
            params.email.length < MIN_EMAIL_LENGTH -> AuthResponse.Error("The 'email' should be at least $MIN_EMAIL_LENGTH characters long")
            !params.email.isValid() -> AuthResponse.Error("Email value should be in [test]@[test].[test] format")
            params.username.length < MIN_USERNAME_LENGTH -> AuthResponse.Error("The 'username' should be at least $MIN_USERNAME_LENGTH characters long")
            params.password.length < MIN_PASSWORD_LENGTH -> AuthResponse.Error("The 'password' should be at least $MIN_PASSWORD_LENGTH characters long")
            else -> {
                try {
                    val user = userRepository.createUser(
                        User(
                            name = params.username.value,
                            email = params.email.value,
                            passwordHash = encryptor.encrypt(params.password)
                        )
                    )
                    if (user != null) {
                        AuthResponse.Created(jwtService.generateToken(user.id))
                    } else throw java.lang.Exception()
                } catch (ex: UserAlreadyExistException) {
                    AuthResponse.Error("User with the following username already exist")
                } catch (ex: Throwable) {
                    AuthResponse.Error("Failed to create user")
                }
            }
        }
    }

    suspend fun login(
        params: LoginRequestParams
    ): AuthResponse {
        return when {
            params.email.length < MIN_EMAIL_LENGTH -> AuthResponse.Error("The 'email' should be at least $MIN_EMAIL_LENGTH characters long")
            params.password.length < MIN_PASSWORD_LENGTH -> AuthResponse.Error("The 'password' should be at least $MIN_EMAIL_LENGTH characters long")
            else -> {
                val user = userRepository.getUserByEmailAndPassword(params.email.value, encryptor.encrypt(params.password))
                if (user != null) {
                    AuthResponse.Success(jwtService.generateToken(user.id))
                } else {
                    AuthResponse.Error("Invalid credentials")
                }
            }
        }
    }
}
