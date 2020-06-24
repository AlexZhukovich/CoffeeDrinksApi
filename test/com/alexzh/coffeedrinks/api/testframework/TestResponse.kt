package com.alexzh.coffeedrinks.api.testframework

import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationResponse

data class TestResponse<T>(
    val statusCode: HttpStatusCode,
    val content: T?
)

fun <T> successResponse(
    content: T
) = TestResponse(HttpStatusCode.OK, content)

fun noContentResponse() = TestResponse(HttpStatusCode.NoContent, null)