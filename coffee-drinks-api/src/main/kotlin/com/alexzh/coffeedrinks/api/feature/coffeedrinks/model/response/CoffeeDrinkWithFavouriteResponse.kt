package com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response

@kotlinx.serialization.Serializable
data class CoffeeDrinkWithFavouriteResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String,
    val isFavourite: Boolean
)
