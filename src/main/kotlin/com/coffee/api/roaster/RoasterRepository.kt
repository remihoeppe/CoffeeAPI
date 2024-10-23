package com.coffee.api.roaster

import com.coffee.api.Roaster
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.transactions.transaction


interface RoasterRepository {
    fun allRoasters(): List<Roaster>
    fun roasterByName(name: String): Roaster?
    fun addRoaster(newRoaster: Roaster): RoasterDAO
    fun removeRoaster(name: String): Boolean
}

class PostgresRoasterRepository : RoasterRepository {
    override fun allRoasters(): List<Roaster> = transaction { RoasterDAO.all().map(::daoToModel) }

    override fun roasterByName(name: String): Roaster? = transaction {
        RoasterDAO.find { RoasterTable.name.lowerCase() eq name.lowercase() }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override fun addRoaster(newRoaster: Roaster): RoasterDAO = transaction {
        RoasterDAO.new {
            name = newRoaster.name
            url = newRoaster.url
            address = newRoaster.address
        }
    }

    override fun removeRoaster(name: String): Boolean {
        val rowsDeleted = RoasterTable.deleteWhere { RoasterTable.name eq name }
        return rowsDeleted == 1;
    }


}
