package com.alexzh.coffeedrinks.api.response

@kotlinx.serialization.Serializable
data class CoffeeDrinkWithoutFavouriteResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String
)
