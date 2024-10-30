package com.coffee.api.coffee

import com.coffee.api.roaster.daoToModel
import org.jetbrains.exposed.sql.transactions.transaction

interface CoffeeRepository {
    fun allCoffees(): List<Coffee>
    fun coffeeByName(name: String): Coffee?
    fun coffeeById(id: String): Coffee?
    fun addCoffee(newCoffee: Coffee)
    fun removeCoffee(id: String): Boolean
}


// TODO - Refactor duplicate dbTransaction
fun <T> dbTransaction(block: () -> T) : T {
    return transaction { block() }
}

class PostgresCoffeeRepository : CoffeeRepository {
    override fun allCoffees(): List<Coffee> {
        return dbTransaction { CoffeeDAO.all().map(::coffeeDAOtoModel)}
    }

    override fun coffeeByName(name: String): Coffee? {
        TODO("Not yet implemented")
    }

    override fun coffeeById(id: String): Coffee? {
        TODO("Not yet implemented")
    }

    override fun addCoffee(newCoffee: Coffee) {
        TODO("Not yet implemented")
    }

    override fun removeCoffee(id: String): Boolean {
        TODO("Not yet implemented")
    }

}
