package com.alexzh.coffeedrinks.api.feature.auth.encryptor

import javax.crypto.spec.SecretKeySpec

interface Encryptor {
    val keySpec: SecretKeySpec

    fun encrypt(value: String): String
}