package com.coffee.api.coffee

import com.coffee.api.roaster.RoasterCoffeeTable
import com.coffee.api.roaster.RoasterDAO
import com.coffee.api.roaster.RoasterTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class CoffeeService {

    fun getCoffeeWithRoasterByName(coffeeName: String): CoffeeWithRoaster? {
        return transaction {
            // Join CoffeeTable, RoasterCoffeeTable, and RoasterTable to find the roaster for the given coffee name
            (CoffeeTable innerJoin RoasterCoffeeTable innerJoin RoasterTable)
                .selectAll().where { CoffeeTable.name eq coffeeName }
                .mapNotNull { row ->
                    CoffeeWithRoaster(
                        coffeeName = row[CoffeeTable.name],
                        roastedBy = row[RoasterTable.name],
                    )
                }
                .firstOrNull()
        }
    }

    fun getCoffeeWithRoasterById(id: Int): CoffeeWithRoaster? {
        return transaction {
            // Join CoffeeTable, RoasterCoffeeTable, and RoasterTable to find the roaster for the given coffee name
            (CoffeeTable innerJoin RoasterCoffeeTable innerJoin RoasterTable)
                .selectAll().where { CoffeeTable.id eq id }
                .mapNotNull { row ->
                    CoffeeWithRoaster(
                        coffeeName = row[CoffeeTable.name],
                        roastedBy = row[RoasterTable.name],
                    )
                }
                .firstOrNull()
        }
    }

    fun createCoffeeWithRoaster(coffeeName: String, roastedBy: String): Boolean {
        return transaction {
            // Find the roaster by name, or return false if not found
            val roaster = RoasterDAO.find { RoasterTable.name eq roastedBy }.firstOrNull()
                ?: return@transaction false // Roaster must exist, otherwise fail

            // Create the coffee entry
            val coffeeId = CoffeeTable.insertAndGetId { it[name] = coffeeName }

            // Create the association in the RoasterCoffeeTable join table
            RoasterCoffeeTable.insert {
                it[RoasterCoffeeTable.roaster] = roaster.id
                it[RoasterCoffeeTable.coffee] = coffeeId
            }

            true // Return true on successful creation
        }
    }

}
