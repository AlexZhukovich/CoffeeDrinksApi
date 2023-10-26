package com.alexzh.coffeedrinks.api.response

@kotlinx.serialization.Serializable
data class CoffeeDrinkWithFavouriteResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String,
    val isFavourite: Boolean
)
