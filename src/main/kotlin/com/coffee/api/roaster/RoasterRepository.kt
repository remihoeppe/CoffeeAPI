package com.coffee.api.roaster

import org.jetbrains.exposed.sql.transactions.transaction

interface RoasterRepository {
    fun allRoasters(): List<Roaster>
    fun roasterByName(name: String): Roaster?
    fun roasterById(id: String): Roaster?
    fun addRoaster(newRoaster: Roaster)
    fun removeRoaster(name: String): Boolean
}

fun <T> dbTransaction(block: () -> T) : T {
    return transaction { block() }
}

/*** Generics (<T>): The function is generic, meaning it can accept and return any type T.
Lambda (block: () -> T): It takes a lambda (or function) as a parameter, which doesn't require input but returns something of type T.
Exposed's transaction {}: The transaction {} block manages database transactions. Inside it, the provided block is executed.
Return: The result of block() (inside the transaction) is returned as the final output of dbTransaction.
Essentially, it's a wrapper that ensures any database interaction (the block) is executed within a transaction. ***/
