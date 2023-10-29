package com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CoffeeDrinkFavouriteResponse(val isFavourite: Boolean)