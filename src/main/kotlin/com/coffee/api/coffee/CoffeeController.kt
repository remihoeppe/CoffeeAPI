package com.coffee.api.coffee

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Moshi.autoBody
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

fun coffeeRoutes(repository: CoffeeRepository) : RoutingHttpHandler {

    val allCoffeesLens = autoBody<List<Coffee>>().toLens()

    return routes (
        "/coffees" bind Method.GET to {
            val coffeeList = repository.allCoffees()
            allCoffeesLens.inject(coffeeList, Response(Status.OK))
        }
    )
}
