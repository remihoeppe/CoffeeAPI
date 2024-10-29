package com.coffee.api.roaster

import org.jetbrains.exposed.dao.id.IntIdTable
import com.coffee.api.coffee.CoffeeTable

object RoasterTable : IntIdTable() {
    val name = varchar("name", 255)
    val url = varchar("url", 255)
    val address = varchar("address", 255)
}

// Define the join table between Roasters and Coffees
object RoasterCoffeeTable : IntIdTable() {
    val roaster = reference("roaster_id", RoasterTable) // Foreign key to RoasterTable
    val coffee = reference("coffee_id", CoffeeTable)    // Foreign key to CoffeeTable
}
