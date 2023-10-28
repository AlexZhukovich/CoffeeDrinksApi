package com.alexzh.coffeedrinks.api.feature.auth.encryptor

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class EncryptorImpl(
    private val secretKey: String
): Encryptor {
    companion object {
        private const val ALGORITHM = "HmacSHA512"
    }
    override val keySpec: SecretKeySpec
        get() = SecretKeySpec(secretKey.toByteArray(), ALGORITHM)

    override fun encrypt(value: String): String {
        val mac = Mac.getInstance(ALGORITHM)
        mac.init(keySpec)
        return hex(mac.doFinal(value.toByteArray(Charsets.UTF_8)))
    }
}