package com.alexzh.coffeedrinks.testframework

import io.ktor.http.HttpStatusCode

data class TestResponse<T>(
    val statusCode: HttpStatusCode,
    val content: T?
)

fun <T> successResponse(
    content: T
) = TestResponse(HttpStatusCode.OK, content)

fun noContentResponse() = TestResponse(HttpStatusCode.NoContent, null)