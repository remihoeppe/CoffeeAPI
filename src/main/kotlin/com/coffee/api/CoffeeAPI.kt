package com.coffee.api

import com.coffee.api.roaster.PostgresRoasterRepository
import com.coffee.api.roaster.Roaster
import com.coffee.api.roaster.roasterRoutes
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.format.Moshi.autoBody
import org.http4k.routing.path
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction


fun main() {

    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mycoffeeapp",
        driver = "org.postgresql.Driver",
        user = "remi",
        password = "postgres",
    )
    transaction {
        addLogger(StdOutSqlLogger) // Add the logger here for global logging
    }
    coffeeAPI().asServer(SunHttp(port = 9000)).start()
}

val allRoastersLens = autoBody<List<Roaster>>().toLens()
val roasterLens = autoBody<Roaster>().toLens()


fun coffeeAPI(): HttpHandler {
    val roasterRepository = PostgresRoasterRepository()
//    val coffeeService = CoffeeService() // Replace with your actual service

    return routes(
        "/" bind Method.GET to { Response(Status.OK).body("Coffee API") },
        roasterRoutes(roasterRepository),
//        coffeeRoutes(coffeeService)
    ).withFilter(DebuggingFilters.PrintRequestAndResponse().then(ServerFilters.CatchAll()))
}
