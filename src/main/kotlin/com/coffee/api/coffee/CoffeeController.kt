package com.coffee.api.coffee

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Moshi.autoBody
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

fun coffeeRoutes(repository: CoffeeRepository, service: CoffeeService): RoutingHttpHandler {

    val allCoffeesLens = autoBody<List<Coffee>>().toLens()
    val coffeeWithRoasterLens = autoBody<CoffeeWithRoaster>().toLens()

    return routes(
        "/coffees" bind Method.GET to {
            val coffeeList = repository.allCoffees()
            allCoffeesLens.inject(coffeeList, Response(Status.OK))
        },

        "/coffees/byName/{name}" bind Method.GET to { request ->
            val name = request.path("name")
            val coffeeWithRoaster = name?.let { service.getCoffeeWithRoasterByName(it) }
            if (coffeeWithRoaster == null) Response(Status.NOT_FOUND)
            else coffeeWithRoasterLens.inject(
                coffeeWithRoaster,
                Response(Status.OK)
            )
        }


    )
}
