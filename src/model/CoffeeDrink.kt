package com.alexzh.coffeedrinks.api.model

import java.io.Serializable

data class CoffeeDrink(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String
) : Serializable