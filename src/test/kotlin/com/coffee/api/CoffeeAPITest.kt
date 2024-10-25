package com.coffee.api

import com.coffee.api.roaster.RoasterTable
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Moshi.json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoffeeAPITest {

    private val api = coffeeAPI()
    private val defaultRoastersJson =
        "{\"roasters\":[{\"name\":\"Monmouth Coffee Company\",\"url\":\"https://www.monmouthcoffee.co.uk/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Square Mile Coffee Roasters\",\"url\":\"https://shop.squaremilecoffee.com/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Skylark Coffee\",\"url\":\"https://skylark.coffee/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Grindsmith\",\"url\":\"https://grindsmith.com/\",\"address\":\"123 Street\"}," +
                "{\"name\":\"Curve Coffee\",\"url\":\"https://www.curveroasters.co.uk/\",\"address\":\"123 Street\"}]}"

    private val grindSmithJson =
        "{\"name\":\"Grindsmith\",\"url\":\"https://grindsmith.com/\",\"address\":\"123 Street\"}"
    private val newRoasterJson =
        "{\"name\":\"Verve Coffee\",\"url\":\"https://vervecoffee.com\",\"address\":\"Santa Cruz, CA\"}"
    val invalidData = "{\"name\":\"\",\"url\":\"\",\"address\":\"\"}"

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
        val response = api(Request(GET, "/roasters/byName/grindsmith"))
        assertEquals(grindSmithJson, response.bodyString())
    }

    @Test
    fun `API should return 404 when an invalid name is send through a GET request`() {
        val response = api(Request(GET, "/roaster/byName/covfefe"))
        response.expectNotFound()
    }

    @Test
    fun `API should return 400 when invalid roaster data is sent`() {
//        Only testing if any of the data field is empty.
//        TODO: Add supplemental checks re valid URL
        val response = api(Request(POST, "/roasters").body(invalidData))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `API should return a 204 when valid data has been sent through a POST request`() {
        api(Request(POST, "/roasters").body(newRoasterJson)).expectNoContent()

    }

    @Test
    fun `API should return 204 when valid name parameter sent through DELETE request`() {
        api(Request(DELETE, "/roasters/grindsmith")).expectNoContent()
    }

    @Test
    fun `API should return 404 when invalid name param sent through DEL request`() {
        api(Request(DELETE, "/roasters/covfefe")).expectNotFound()
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
            transaction {
                SchemaUtils.drop(RoasterTable)
                SchemaUtils.create(RoasterTable)
                RoasterTable.insert {
                    it[name] = "Monmouth Coffee Company"
                    it[url] = "https://www.monmouthcoffee.co.uk/"
                    it[address] = "123 Street"
                }
                RoasterTable.insert {
                    it[name] = "Square Mile Coffee Roasters"
                    it[url] = "https://shop.squaremilecoffee.com/"
                    it[address] = "123 Street"
                }
                RoasterTable.insert {
                    it[name] = "Skylark Coffee"
                    it[url] = "https://skylark.coffee/"
                    it[address] = "123 Street"
                }
                RoasterTable.insert {
                    it[name] = "Grindsmith"
                    it[url] = "https://grindsmith.com/"
                    it[address] = "123 Street"
                }
                RoasterTable.insert {
                    it[name] = "Curve Coffee"
                    it[url] = "https://www.curveroasters.co.uk/"
                    it[address] = "123 Street"
                }
            }
            println("Database prepared successfully")
        }
    }
}


private fun Response.expectOK(): Response {
    assertEquals(OK, this.status)
    return this
}

private fun Response.expectNoContent(): Response {
    assertEquals(NO_CONTENT, this.status)
    return this
}

private fun Response.expectNotFound(): Response {
    assertEquals(NOT_FOUND, this.status)
    return this
}
