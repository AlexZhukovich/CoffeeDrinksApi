package com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.param

import kotlinx.serialization.Serializable

@Serializable
data class CoffeeDrinkFavoriteRequestParam(
    val isFavorite: Boolean
)
