package com.alexzh.coffeedrinks.api.data.model

@kotlinx.serialization.Serializable
data class CoffeeDrink(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String,
    val isFavourite: Boolean
)