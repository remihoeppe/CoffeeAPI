package com.coffee.api.roaster

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

object RoasterTable : IntIdTable() {
    val name = varchar("name", 255)
    val url = varchar("url", 255)
    val address = varchar("address", 255)
}

class RoasterDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoasterDAO>(RoasterTable)

    var name by RoasterTable.name
    var url by RoasterTable.url
    var address by RoasterTable.address
}

fun <T> dbTransaction(block: () -> T) : T {
    return transaction { block() }
}

/*** Generics (<T>): The function is generic, meaning it can accept and return any type T.
Lambda (block: () -> T): It takes a lambda (or function) as a parameter, which doesn't require input but returns something of type T.
Exposed's transaction {}: The transaction {} block manages database transactions. Inside it, the provided block is executed.
Return: The result of block() (inside the transaction) is returned as the final output of dbTransaction.
Essentially, it's a wrapper that ensures any database interaction (the block) is executed within a transaction. ***/

fun daoToModel(dao: RoasterDAO) = Roaster(
    dao.name,
    dao.url,
    dao.address
)
