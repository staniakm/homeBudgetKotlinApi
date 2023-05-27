package com.example.demo.controller

import com.example.demo.IntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AssortmentControllerTest : IntegrationTest() {

    @Test
    fun `should return assortment details`() {
        // given
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        // when
        val response = methodUnderTest("should return assortment details") {
            restTemplate.getForEntity("/api/assortment/1", AssortmentDetailsResponse::class.java)
        }

        // then
        response.statusCode shouldBe HttpStatus.OK
        with(response.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 1
        }
    }

    @Test
    fun `should return not found when assortment not exists`() {
        //given
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        // when
        val response = methodUnderTest("should return bad request when assortment not exists") {
            restTemplate.getForEntity("/api/assortment/2", String::class.java)
        }

        // then
        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `should change assortment category`() {
        // given
        createCategory(1, "cat1")
        createCategory(2, "cat2")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(1, 2)

        // when
        val newAssortment = methodUnderTest("should change assortment category") {
            restTemplate.postForEntity("/api/assortment/change-category",
                request,
                AssortmentDetailsResponse::class.java)
        }

        // then
        newAssortment.statusCode shouldBe HttpStatus.OK
        with(newAssortment.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 2
        }

        // and when get
        val response = methodUnderTest("should change assortment category") {
            restTemplate.getForEntity("/api/assortment/1", AssortmentDetailsResponse::class.java)
        }
        response.statusCode shouldBe HttpStatus.OK
        with(response.body!!) {
            id shouldBe 1
            name shouldBe "aso1"
            categoryId shouldBe 2
        }
    }

    @Test
    fun `should return bad request when assortment not exists`() {
// given
        createCategory(1, "cat1")
        createCategory(2, "cat2")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(2, 2)

        // when
        val newAssortment = methodUnderTest("should change assortment category") {
            restTemplate.postForEntity("/api/assortment/change-category",
                request,
                AssortmentDetailsResponse::class.java)
        }

        // then
        newAssortment.statusCode shouldBe HttpStatus.BAD_REQUEST
    }
    @Test
    fun `should return bad request when destination category not exists`() {
// given
        createCategory(1, "cat1")
        createAssortment(1, "aso1", 1)

        val request = AssortmentChangeCategoryRequest(2, 2)

        // when
        val newAssortment = methodUnderTest("should change assortment category") {
            restTemplate.postForEntity("/api/assortment/change-category",
                request,
                AssortmentDetailsResponse::class.java)
        }

        // then
        newAssortment.statusCode shouldBe HttpStatus.BAD_REQUEST
    }

    @Test
    fun `should merge two assortments into one with recalculation of expenses`() {

    }

    @Test
    fun `should return bad request when destination assortment not exists`() {

    }

    @Test
    fun `should return bad request when source assortment not exists`() {

    }

}