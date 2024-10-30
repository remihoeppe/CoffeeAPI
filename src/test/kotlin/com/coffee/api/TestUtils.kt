package com.coffee.api

import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals

object TestUtils {
    fun Response.expectOK(): Response {
        assertEquals(OK, this.status)
        return this
    }

    fun Response.expectNoContent(): Response {
        assertEquals(NO_CONTENT, this.status)
        return this
    }

    fun Response.expectNotFound(): Response {
        assertEquals(NOT_FOUND, this.status)
        return this
    }
}
