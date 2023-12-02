package com.alexzh.coffeedrinks.api.data

import com.alexzh.coffeedrinks.api.data.db.table.CoffeeDrinksTable
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun predefinedCoffeeDrinks() = listOf<CoffeeDrinksTable.(InsertStatement<Number>) -> Unit>(
    {
        it[name] = "Americano"
        it[imageUrl] = "americano.png"
        it[description] = "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee."
        it[ingredients] = "Espresso, Water"
    },
    {
        it[name] = "Cappuccino"
        it[imageUrl] = "cappuccino.png"
        it[description] = "A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with steamed milk foam."
        it[ingredients] = "Espresso, Steamed milk foam"
    },
    {
        it[name] = "Cold-brew coffee"
        it[imageUrl] = "cold-brew-coffee.png"
        it[description] = "Cold-brew coffee is coffee, brewed cold."
        it[ingredients] = "Coffee beans, Water"
    },
    {
        it[name] = "Espresso"
        it[imageUrl] = "espresso.png"
        it[description] = "Espresso is coffee of Italian origin, brewed by forcing a small amount of nearly boiling water under pressure (expressing) through finely-ground coffee beans."
        it[ingredients] = "Ground coffee, Water"
    },
    {
        it[name] = "Espresso Macchiato"
        it[imageUrl] = "espresso-macchiato.png"
        it[description] = "Espresso Macchiato is a coffee beverage (a single or double espresso topped with a dollop of heated, foamed milk)."
        it[ingredients] = "Espresso, Foamed milk"
    },
    {
        it[name] = "Frappino"
        it[imageUrl] = "frappino.png"
        it[description] = "Frappino is a blended coffee drinks. It consists of coffee base, blended with ice and other various ingredients, usually topped with whipped cream."
        it[ingredients] = "Espresso, Cold milk, Sugar, Ice cubes, Irish Cream flavoured syrup, Whipped cream, Chocolate sauce"
    },
    {
        it[name] = "Gingerbread coffee"
        it[imageUrl] = "gingerbread-coffee.png"
        it[description] = "Gingerbread coffee is a coffee beverage with aroma of Christmas evenings."
        it[ingredients] = "Molasses, Brown Sugar, Baking soda, Ground ginger, Ground Cinnamon, Hot brewed coffee, Whipped cream, Ground cloves"
    },
    {
        it[name] = "Iced Mocha"
        it[imageUrl] = "iced-mocha.png"
        it[description] = "Iced Mocha is a coffee beverage. It based on Espresso and chocolate syrup with cold milk, foam and whipped cream or ice cream."
        it[ingredients] = "Cold coffee, Milk, Chocolate syrup, Whipped cream, Ice cream"
    },
    {
        it[name] = "Irish coffee"
        it[imageUrl] = "irish-coffee.png"
        it[description] = "Irish coffee is a cocktail consisting of hot coffee, Irish whiskey, and sugar stirred, and topped with cream."
        it[ingredients] = "Irish whiskey, Hot strong brewed coffee, Heavy whipping cream, Sugar, Creme de menthe liqueur"
    },
    {
        it[name] = "Latte"
        it[imageUrl] = "latte.png"
        it[description] = "A latte is a coffee drink made with espresso and steamed milk."
        it[ingredients] = "Espresso, Steamed milk"
    },
    {
        it[name] = "Latte Macchiato"
        it[imageUrl] = "latte-macchiato.png"
        it[description] = "Latte Macchiato is a coffee beverage; the name literally means stained milk."
        it[ingredients] = "Espresso, Milk, Milk foam, Flavoured coffee syrup"
    },
    {
        it[name] = "Mocha"
        it[imageUrl] = "mocha.png"
        it[description] = "A Mocha, also called mocaccino, is a chocolate-flavored variant of a Latte."
        it[ingredients] = "Espresso, Chocolate flavoring"
    },
    {
        it[name] = "Turkish coffee"
        it[imageUrl] = "turkish-coffee.png"
        it[description] = "Turkish coffee is coffee prepared using very finely ground coffee beans, unfiltered."
        it[ingredients] = "Water, Extra fine ground coffee, Ground cardamom"
    },
)