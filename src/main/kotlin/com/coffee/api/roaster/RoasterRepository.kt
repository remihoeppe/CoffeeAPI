package com.coffee.api.roaster

interface RoasterRepository {
    fun allRoasters(): List<Roaster>
    fun roasterByName(name: String): Roaster?
    fun roasterById(id: String): Roaster?
    fun addRoaster(newRoaster: Roaster)
    fun removeRoaster(name: String): Boolean
}
