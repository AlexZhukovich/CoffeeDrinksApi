package com.alexzh.coffeedrinks.api.testframework

import io.ktor.server.testing.TestApplicationResponse

inline fun <reified T> TestApplicationResponse.toTestResponse(): TestResponse<T> {
    return TestResponse(
            this.status()!!,
            createJsonObjectOf(this.content)
    )
}

inline fun <reified T> TestApplicationResponse.toTestResponseOfList(): TestResponse<List<T>> {
    return TestResponse(
            this.status()!!,
            createJsonListOf(this.content)
    )
}

inline fun <reified T> TestApplicationResponse.toTestResponseOfNullableContent(): TestResponse<T?> {
    return TestResponse(
            this.status()!!,
            null
    )
}