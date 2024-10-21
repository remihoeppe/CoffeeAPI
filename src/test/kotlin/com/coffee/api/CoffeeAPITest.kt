package com.coffee.api

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoffeeAPITest {

    private val api = coffeeAPI()

    @Test
    fun `Testing server root endpoint`() {
        api(Request(GET, "/")).expectOK()
    }

    @Test
    fun `API returns a list of roasters GET request`() {
        val response = api(Request(GET, "/roasters")).expectOK()
        assertEquals("some json", response.bodyString())
    }

    @Test
    fun `API returns a specific roaster when GET requests contains a name parameter`() {
        val response = api(Request(GET, "/roasters/{name}"))
        assertEquals(Roaster("name", "url", "address"), response.bodyString())
    }

    @Test
    fun `API should return 404 when invalid name parameter is passed on GET request`() {
        val invalidRoasterName = "invalid"
        val response = api(Request(GET, "/roasters/$invalidRoasterName"))
        assertEquals(NOT_FOUND, response.status)
    }

    @Test
    fun `API should return a 204 when valid data has been sent through a POST request`() {
        val data = Roaster("name", "url", "address")
        val response = api(Request(POST, "/roasters", ).body(data))
    }



}

private fun Response.expectOK(): Response {
    assertEquals(OK, this.status)
    return this
}
