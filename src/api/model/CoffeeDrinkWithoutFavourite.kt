package com.alexzh.coffeedrinks.api.api.model

import java.io.Serializable

data class CoffeeDrinkWithoutFavourite(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val ingredients: String
) : Serializable