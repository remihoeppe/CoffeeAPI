package com.coffee.api.coffee

import com.coffee.api.roaster.RoasterCoffeeTable
import com.coffee.api.roaster.RoasterTable
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

    
}
