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
    val newCoffeeRequestLens = autoBody<NewCoffeeRequest>().toLens()

    return routes(
        "/coffees" bind Method.GET to {
            val coffeeList = repository.allCoffees()
            allCoffeesLens.inject(coffeeList, Response(Status.OK))
        },

        "/coffees" bind Method.POST to { request ->
            val newCoffeeRequest = newCoffeeRequestLens.extract(request)
            val success = service.createCoffeeWithRoaster(newCoffeeRequest.coffeeName, newCoffeeRequest.roastedBy)
            if (success) {
                Response(Status.CREATED, "New coffee created!")
            } else {
                Response(Status.BAD_REQUEST, "Roaster not found")
            }
        },

        "/coffees/byName/{name}" bind Method.GET to { request ->
            val name = request.path("name")
            val coffeeWithRoaster = name?.let { service.getCoffeeWithRoasterByName(it) }
            if (coffeeWithRoaster == null) Response(Status.NOT_FOUND)
            else coffeeWithRoasterLens.inject(
                coffeeWithRoaster,
                Response(Status.OK)
            )
        },

        "/coffees/byId/{id}" bind Method.GET to { request ->
            val id = request.path("id")?.toInt()
            val coffeeWithRoaster = id?.let { service.getCoffeeWithRoasterById(it) }
            if (coffeeWithRoaster == null) Response(Status.NOT_FOUND)
            else coffeeWithRoasterLens.inject(
                coffeeWithRoaster,
                Response(Status.OK)
            )
        }


    )
}
