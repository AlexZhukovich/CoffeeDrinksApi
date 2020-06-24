package com.alexzh.coffeedrinks.api.testframework

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> createJsonListOf(
    content: String?
): List<T> {
    return Gson()
        .fromJson(
            content,
            TypeToken.getParameterized(List::class.java, T::class.java).type
        )
}

inline fun <reified T> createJsonObjectOf(
    content: String?
): T? {
    return Gson()
            .fromJson(
                content,
                T::class.java
            )
}