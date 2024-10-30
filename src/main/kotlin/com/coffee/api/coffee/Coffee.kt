package com.coffee.api.coffee

data class Coffee(val name: String)

data class CoffeeWithRoaster(
    val coffeeName: String,
    val roastedBy: String
)
