package com.alexzh.coffeedrinks.api.auth

import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@KtorExperimentalAPI
fun hashKey(environment: ApplicationEnvironment) =
        hex(environment.config.property("api.hashKey").getString())

@KtorExperimentalAPI
fun hmacKey(environment: ApplicationEnvironment) =
        SecretKeySpec(hashKey(environment), "HmacSHA1")

@KtorExperimentalAPI
fun hash(environment: ApplicationEnvironment, password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey(environment))
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}