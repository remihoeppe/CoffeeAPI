package com.coffee.api.coffee

import com.coffee.api.roaster.RoasterTable
import org.jetbrains.exposed.dao.id.IntIdTable

object CoffeeTable : IntIdTable() {
    val name = varchar("name", 255)
}
