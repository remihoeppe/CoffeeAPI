package com.coffee.api.roaster

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase

class PostgresRoasterRepository : RoasterRepository {
    override fun allRoasters(): List<Roaster> = dbTransaction { RoasterDAO.all().map(::daoToModel) }

    override fun roasterByName(name: String): Roaster? =
        dbTransaction {
            RoasterDAO.find { RoasterTable.name.lowerCase() eq name.lowercase() }
                .limit(1)
                .map(::daoToModel)
                .firstOrNull()
        }


    override fun roasterById(id: String): Roaster? = dbTransaction {
        RoasterDAO.find { RoasterTable.id eq id.toInt() }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override fun addRoaster(newRoaster: Roaster): Unit = dbTransaction {
        RoasterDAO.new {
            name = newRoaster.name
            url = newRoaster.url
            address = newRoaster.address
        }
    }

    override fun removeRoaster(name: String): Boolean = dbTransaction {
        val rowsDeleted = RoasterTable.deleteWhere { RoasterTable.name.lowerCase() eq name.lowercase() }
        rowsDeleted == 1;
    }
}
