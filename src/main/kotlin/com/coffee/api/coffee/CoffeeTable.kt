package com.coffee.api.coffee

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CoffeeTable : IntIdTable() {
    val name = varchar("name", 255)
}

class CoffeeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CoffeeDAO>(CoffeeTable)
    var name by CoffeeTable.name
}
