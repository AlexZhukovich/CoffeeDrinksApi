package api.coffeedrinks.mapper

import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithFavouriteResponse
import com.alexzh.coffeedrinks.api.api.coffeedrinks.model.CoffeeDrinkWithoutFavouriteResponse
import com.alexzh.coffeedrinks.api.data.model.CoffeeDrink

class CoffeeDrinkMapper {

    fun mapToCoffeeDrinkWithFavourite(
            coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithFavouriteResponse(
            coffeeDrink.id,
            coffeeDrink.name,
            coffeeDrink.imageUrl,
            coffeeDrink.description,
            coffeeDrink.ingredients,
            coffeeDrink.isFavourite
    )

    fun mapToCoffeeDrinkWithoutFavourite(
            coffeeDrink: CoffeeDrink
    ) = CoffeeDrinkWithoutFavouriteResponse(
            coffeeDrink.id,
            coffeeDrink.name,
            coffeeDrink.imageUrl,
            coffeeDrink.description,
            coffeeDrink.ingredients
    )
}