package com.alexzh.coffeedrinks.api.api.coffeedrinks.model

import java.io.Serializable

sealed class CoffeeDrinkResponse {

    data class CoffeeDrinkWithFavourite(
            val id: Long,
            val name: String,
            val imageUrl: String,
            val description: String,
            val ingredients: String,
            val isFavourite: Boolean
    ) : Serializable, CoffeeDrinkResponse()

    data class CoffeeDrinkWithoutFavourite(
            val id: Long,
            val name: String,
            val imageUrl: String,
            val description: String,
            val ingredients: String
    ) : Serializable, CoffeeDrinkResponse()
}