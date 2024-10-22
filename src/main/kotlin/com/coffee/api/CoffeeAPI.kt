package com.coffee.api


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

fun main() {
    coffeeAPI().asServer(SunHttp(port = 9000)).start()
}

val roastersLens = autoBody<Roasters>().toLens()
val roasterLens = autoBody<Roaster>().toLens()

fun coffeeAPI() : HttpHandler {
    var roasters = mutableListOf(
        Roaster("Monmouth Coffee Company", "https://www.monmouthcoffee.co.uk/", "123 Street"),
        Roaster("Square Mile Coffee Roasters", "https://shop.squaremilecoffee.com/", "123 Street"),
        Roaster("Skylark Coffee", "https://skylark.coffee/", "123 Street"),
        Roaster("Grindsmith", "https://grindsmith.com/", "123 Street"),
        Roaster("Curve Coffee", "https://www.curveroasters.co.uk/", "123 Street"),
    )

    return routes(
        "/" bind GET to {
            Response(OK).body("Coffee API")
        },

        "/roasters" bind GET to {
            val roastersList = Roasters(roasters)
            roastersLens.inject(roastersList, Response(OK))
        },

        "/roasters/{name}" bind GET to { request ->
            val name = request.path("name")
            val roaster = roasters.find { it.name.equals(name, ignoreCase = true) }
            roaster?.let {
                roasterLens.inject(roaster, Response(OK))
            }?:Response(NOT_FOUND)
        },

        "/roasters" bind Method.POST to { request ->
            try {
                val newRoasters = roastersLens.extract(request).roasters
                val invalidRoasters = newRoasters.filter { it.name.isNotBlank() || !isValidUrl(it.url) }
                if (invalidRoasters.isNotEmpty()) {
                    Response(Status.BAD_REQUEST).body("Invalid roaster data")
                }
                roasters.addAll(newRoasters)
                Response(NO_CONTENT)
            } catch (e: Exception) {
                Response(Status.BAD_REQUEST).body("Invalid Data")
            }
        }


//
    ).withFilter(DebuggingFilters.PrintRequestAndResponse().then(ServerFilters.CatchAll()))
}


// The name of the list variable in Roasters is going to be the name of the JSON array in the GET response
data class Roasters(val roasters: List<Roaster> = emptyList())
data class Roaster(val name: String, val url: String, val address: String)

fun isValidUrl(url: String): Boolean {
    return try {
        val uri = java.net.URI(url)
        uri.scheme == "http" || uri.scheme == "https"
    } catch (e: Exception) {
        false
    }
}
