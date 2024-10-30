package com.coffee.api


import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import com.coffee.api.TestUtils.expectNoContent
import com.coffee.api.TestUtils.expectNotFound
import com.coffee.api.TestUtils.expectOK
import com.coffee.api.coffee.CoffeeWithRoaster
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll

class CoffeeTest {
    private val api = coffeeAPI()

    @Test
    fun `API returns a list of Coffees on GET request for root endpoint`() {
        val response = api(Request(Method.GET, "/coffees")).expectOK()
        val responseBody = response.bodyString()
        val json = jacksonObjectMapper().readTree(responseBody)
        assertTrue(json.isArray)
    }

    @Test
    fun `API returns the right coffee when a name parameter is send on GET requests`() {
        val response = api(Request(Method.GET, "/coffees/byName/Good Coffee")).expectOK()
        val responseBody = response.bodyString()
        val coffeeWithRoaster: CoffeeWithRoaster = jacksonObjectMapper().readValue(responseBody)
        assertEquals("Good Coffee", coffeeWithRoaster.coffeeName)
        assertEquals("Monmouth Coffee Company", coffeeWithRoaster.roastedBy)
    }

    @Test
    fun `API returns the right coffee when an ID parameter is sent on a GET request`() {

    }

    @Test
    fun `API returns 404 when an invalid name param is sent on GET request`() {

    }

    @Test
    fun `API returns 404 when an invalid ID param is sent on GET request`() {

    }

    @Test
    fun `API returns 201 on POST request with valid data`() {

    }

    @Test
    fun `API returns 400 on POST request with invalid data`() {

    }

    @Test
    fun `API returns all Coffees for a Roaster when valid Roaster name parameter`() {

    }

    @Test
    fun `API returns all Coffees, as a JSON list, for a Roaster when valid Roaster ID parameter`() {

    }

    @Test
    fun `API returns 404 when invalid name param given on GET Coffees request`() {

    }

    @Test
    fun `API returns 404 when invalid ID param given on GET Coffees request`() {

    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun dbConnect(): Unit {
            Database.connect(
                url = "jdbc:postgresql://localhost:5432/mycoffeeapp",
                driver = "org.postgresql.Driver",
                user = "remi",
                password = "postgres",
            )
        }
    }

}
