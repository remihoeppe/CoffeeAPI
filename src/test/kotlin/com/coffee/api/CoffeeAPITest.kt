package com.coffee.api

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Moshi.json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoffeeAPITest {

    private val api = coffeeAPI()
    private val defaultRoastersJson =
        "{\"roasters\":[{\"name\":\"Monmouth Coffee Company\",\"url\":\"https://www.monmouthcoffee.co.uk/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Square Mile Coffee Roasters\",\"url\":\"https://shop.squaremilecoffee.com/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Skylark Coffee\",\"url\":\"https://skylark.coffee/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Grindsmith\",\"url\":\"https://grindsmith.com/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Curve Coffee\",\"url\":\"https://www.curveroasters.co.uk/\",\"address\":\"123 Street\"}]}"

    private val grindSmithJson = "{\"name\":\"Grindsmith\",\"url\":\"https://grindsmith.com/\",\"address\":\"123 Street\"}"
    private val newRoasterJson ="{\"roasters\":[{\"name\":\"Verve Coffee\",\"url\":\"https://vervecoffee.com\",\"address\":\"Santa Cruz, CA\"}]}"
    val invalidData = "{\"roasters\":[{\"name\":\"\",\"url\":\"\",\"address\":\"\"}]}"

    @Test
    fun `Testing server root endpoint`() {
        api(Request(GET, "/")).expectOK()
    }

    @Test
    fun `API returns a list of roasters GET request`() {
        val response = api(Request(GET, "/roasters")).expectOK()
        assertEquals(defaultRoastersJson, response.bodyString())
    }

    @Test
    fun `API returns a specific roaster when GET requests contains a name parameter`() {
        val response = api(Request(GET, "/roasters/grindsmith"))
        assertEquals(grindSmithJson, response.bodyString())
    }

    @Test
    fun `API should return 400 when invalid roaster data is sent`() {
        val response = api(Request(POST, "/roasters").body(invalidData))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `API should return a 204 when valid data has been sent through a POST request`() {
        val response = api(Request(POST, "/roasters").body(newRoasterJson))
        assertEquals(NO_CONTENT, response.status)
    }


}

private fun Response.expectOK(): Response {
    assertEquals(OK, this.status)
    return this
}