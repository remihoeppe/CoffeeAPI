package com.coffee.api.roaster

import com.coffee.api.roasterLens
import com.coffee.api.allRoastersLens
import org.http4k.core.*
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

fun roasterRoutes(repository: PostgresRoasterRepository): RoutingHttpHandler {
    return routes(
        "/roasters" bind Method.GET to {
            val roastersList = repository.allRoasters()
            allRoastersLens.inject(roastersList, Response(Status.OK))
        },

        "/roasters/byName/{name}" bind Method.GET to { request ->
            val name = request.path("name")
            val roaster = name?.let { repository.roasterByName(it) }
            if (roaster == null) Response(Status.NOT_FOUND) else roasterLens.inject(roaster, Response(Status.OK))
        },

        "/roasters/byId/{id}" bind Method.GET to { request ->
            val id = request.path("id")
            val roaster = id?.let { repository.roasterById(it) }
            if (roaster == null) Response(Status.NOT_FOUND) else roasterLens.inject(roaster, Response(Status.OK))
        },

        "/roasters" bind Method.POST to { request ->
            runCatching {
                val newRoaster = roasterLens.extract(request)
                if (newRoaster.isValid()) {
                    repository.addRoaster(newRoaster)
                    Response(Status.NO_CONTENT)
                } else {
                    Response(Status.BAD_REQUEST).body("Invalid roaster data")
                }
            }.getOrElse {
                Response(Status.BAD_REQUEST).body("Invalid Data")
            }
        },

        "/roasters/{name}" bind Method.DELETE to { request ->
            val name = request.path("name")
            val roaster = name?.let { repository.roasterByName(it) }
            if (roaster != null) {
                repository.removeRoaster(name)
                Response(Status.NO_CONTENT)
            } else {
                Response(Status.NOT_FOUND)
            }
        }
    )
}

fun Roaster.isValid(): Boolean {
    val (name, url, address) = this
    return name.isNotEmpty() && url.isNotEmpty() && address.isNotEmpty()
}
