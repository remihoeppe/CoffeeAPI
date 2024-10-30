package com.coffee.api.coffee

import com.coffee.api.roaster.RoasterDAO
import com.coffee.api.roaster.roasterDAOToModel

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CoffeeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CoffeeDAO>(CoffeeTable)
    var name by CoffeeTable.name
}

fun coffeeDAOtoModel(dao: CoffeeDAO) = Coffee(
    dao.name
)