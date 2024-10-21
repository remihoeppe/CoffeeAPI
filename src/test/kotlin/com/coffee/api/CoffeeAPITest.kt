package com.coffee.api

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoffeeAPITest {

    private val api = coffeeAPI()

    @Test
    fun `Testing server root endpoint`() {
        api(Request(GET, "/")).expectOK()
    }
}

private fun Response.expectOK(): Response {
    assertEquals(OK, this.status)
    return this
}
