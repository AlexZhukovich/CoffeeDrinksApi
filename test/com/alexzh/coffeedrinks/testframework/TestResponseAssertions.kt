package com.alexzh.coffeedrinks.testframework

import io.ktor.server.testing.TestApplicationResponse
import kotlin.test.assertEquals

fun <T> assertResponse(
    expectedResponse: TestResponse<T>,
    actualResponse: TestResponse<T>
) {
    assertEquals(expectedResponse.statusCode, actualResponse.statusCode)
    assertEquals(expectedResponse.content, actualResponse.content)
}

fun <T> assertStatusCodeOfResponse(
        expectedResponse: TestResponse<T>,
        actualResponse: TestApplicationResponse
) {
    assertEquals(expectedResponse.statusCode, actualResponse.status())
}