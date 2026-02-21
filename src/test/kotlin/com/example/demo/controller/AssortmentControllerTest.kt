package com.example.demo.controller

import com.example.demo.IntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AssortmentControllerTest : IntegrationTest() {

    @Test
    fun `should return empty assortment list`() {
        val response = restTemplate.getForEntity("/api/assortment", Array<AssortmentResponse>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0

        AssortmentResponse(1).id shouldBe 1
    }

    @Test
    fun `should return assortment details`() {
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        val response = restTemplate.getForEntity("/api/assortment/1", AssortmentDetailsResponse::class.java)

        response.statusCode shouldBe HttpStatus.OK
        with(response.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 1
        }
    }

    @Test
    fun `should return not found when assortment not exists`() {
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        val response = restTemplate.getForEntity("/api/assortment/2", String::class.java)

        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `should change assortment category`() {
        createCategory(1, "cat1")
        createCategory(2, "cat2")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(1, 2)

        val newAssortment = restTemplate.postForEntity(
            "/api/assortment/change-category",
            request,
            AssortmentDetailsResponse::class.java
        )

        newAssortment.statusCode shouldBe HttpStatus.OK
        with(newAssortment.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 2
        }

        val response = restTemplate.getForEntity("/api/assortment/1", AssortmentDetailsResponse::class.java)
        response.statusCode shouldBe HttpStatus.OK
        with(response.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 2
        }
    }

    @Test
    fun `should return bad request when assortment not exists`() {
        createCategory(1, "cat1")
        createCategory(2, "cat2")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(2, 2)

        val newAssortment = restTemplate.postForEntity(
            "/api/assortment/change-category",
            request,
            AssortmentDetailsResponse::class.java
        )

        newAssortment.statusCode shouldBe HttpStatus.BAD_REQUEST
    }
    @Test
    fun `should return bad request when destination category not exists`() {
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(2, 2)

        val newAssortment = restTemplate.postForEntity(
            "/api/assortment/change-category",
            request,
            AssortmentDetailsResponse::class.java
        )

        newAssortment.statusCode shouldBe HttpStatus.BAD_REQUEST
    }

    @Test
    @Disabled("Not implemented yet")
    fun `should merge two assortments into one with recalculation of expenses`() {

    }

    @Test
    @Disabled("Not implemented yet")
    fun `should return bad request when destination assortment not exists`() {

    }

    @Test
    @Disabled("Not implemented yet")
    fun `should return bad request when source assortment not exists`() {

    }

}
