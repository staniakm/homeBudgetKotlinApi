package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.UpdateAccountDto
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.math.BigDecimal

class ErrorHandlerControllerTest : IntegrationTest() {

    @Test
    fun `should return standardized bad request response for illegal argument`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.account(1, BigDecimal("100.00"), "account1")

        val response = restTemplate.exchange(
            "/api/account/2",
            HttpMethod.PUT,
            HttpEntity(UpdateAccountDto(3, "new name", BigDecimal("100.00"))),
            ApiErrorResponse::class.java
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body!!.code shouldBe "BAD_REQUEST"
        response.body!!.message shouldBe "Invalid requested id"
        response.body!!.path shouldBe "/api/account/2"
        response.body!!.timestamp.shouldNotBeBlank()
    }

    @Test
    fun `should return standardized bad request response for malformed request body`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val response = restTemplate.exchange(
            "/api/account/1",
            HttpMethod.PUT,
            HttpEntity("{invalid-json}", headers),
            ApiErrorResponse::class.java
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body!!.code shouldBe "BAD_REQUEST"
        response.body!!.message shouldBe "Malformed request body"
        response.body!!.path shouldBe "/api/account/1"
        response.body!!.timestamp.shouldNotBeBlank()
    }
}
