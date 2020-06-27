package com.alexzh.coffeedrinks.api.api.coffeedrinks.model

import java.io.Serializable

data class CoffeeDrinkWithoutFavouriteResponse(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val description: String,
        val ingredients: String
) : Serializable