package com.alexzh.coffeedrinks.api.feature.coffeedrinks.model.endpoint

import com.alexzh.coffeedrinks.api.common.BASE_URL
import io.ktor.server.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("${BASE_URL}/coffee-drinks")
class CoffeeDrinks()
