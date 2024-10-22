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

fun coffeeAPI(): HttpHandler {
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
            } ?: Response(NOT_FOUND)
        },

        "/roasters" bind Method.POST to { request ->
            runCatching {
                val newRoaster = roasterLens.extract(request)
                if(newRoaster.isValid()) {
                    roasters.add(newRoaster)
                    Response(NO_CONTENT)
                } else {
                    Response(Status.BAD_REQUEST).body("Invalid roaster data")
                }
            }.getOrElse {
                Response(Status.BAD_REQUEST).body("Invalid Data")
            }
        },

        "/roasters/{name}" bind Method.DELETE to { request ->
            val name = request.path("name")
            val roaster = roasters.find { it.name.equals(name, ignoreCase = true) }
            if(roaster != null) {
                roasters.remove(roaster)
                Response(NO_CONTENT)
            } else {
                Response(NOT_FOUND)
            }
        }

    ).withFilter(DebuggingFilters.PrintRequestAndResponse().then(ServerFilters.CatchAll()))
}


// The name of the list variable in Roasters is going to be the name of the JSON array in the GET response
data class Roasters(val roasters: List<Roaster> = emptyList())
data class Roaster(val name: String, val url: String, val address: String)


fun Roaster.isValid(): Boolean {
    val (name, url, address) = this
    return name.isNotEmpty() && url.isNotEmpty() && address.isNotEmpty()
}
