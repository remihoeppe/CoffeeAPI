package com.coffee.api.roaster

import com.coffee.api.coffee.Coffee
import com.coffee.api.coffee.CoffeeDAO
import com.coffee.api.coffee.CoffeeTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


class RoasterDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoasterDAO>(RoasterTable)

    var name by RoasterTable.name
    var url by RoasterTable.url
    var address by RoasterTable.address
    var coffees by CoffeeDAO via RoasterCoffeeTable
}

fun daoToModel(dao: RoasterDAO) = Roaster(
    dao.name,
    dao.url,
    dao.address,
    coffees = dao.coffees.map { Coffee(it.name) }
)
