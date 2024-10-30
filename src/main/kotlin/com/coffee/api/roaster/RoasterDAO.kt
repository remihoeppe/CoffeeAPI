package com.coffee.api.roaster

import com.coffee.api.coffee.Coffee
import com.coffee.api.coffee.CoffeeDAO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID



class RoasterDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoasterDAO>(RoasterTable)
    var name by RoasterTable.name
    var url by RoasterTable.url
    var address by RoasterTable.address
    var coffees by CoffeeDAO via RoasterCoffeeTable
}

fun roasterDAOToModel(dao: RoasterDAO): Roaster {
    return Roaster(
        name = dao.name,
        url = dao.url,
        address = dao.address,
        coffees = dao.coffees.map { Coffee(it.name) }
    )
}
