package com.coffee.api.roaster

import com.coffee.api.coffee.Coffee

data class Roaster(
    val name: String,
    val url: String,
    val address: String,
    val coffees: List<Coffee> = listOf()
)
