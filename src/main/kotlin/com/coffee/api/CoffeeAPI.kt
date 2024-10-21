package com.coffee.api


import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    coffeeAPI().asServer(SunHttp(port = 9000)).start()
}

fun coffeeAPI() = routes(
    "/" bind GET to {
        Response(OK).body("Coffee API")
    }
).withFilter(DebuggingFilters.PrintRequestAndResponse().then(ServerFilters.CatchAll()))
