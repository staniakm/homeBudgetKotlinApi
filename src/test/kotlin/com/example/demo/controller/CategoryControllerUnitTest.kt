package com.example.demo.controller

import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.CategorySummary
import com.example.demo.service.CategoryService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class CategoryControllerUnitTest {

    @Test
    fun `should return categories summary response`() {
        val service = mock(CategoryService::class.java)
        val expected = listOf(CategorySummary(1, "Food", BigDecimal.ONE, BigDecimal.TEN))
        `when`(service.getCategoriesSummary(0, false)).thenReturn(expected)

        val controller = CategoryController(service)
        val response = controller.getCategoriesSummary(0, false)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }

    @Test
    fun `should return category details response`() {
        val service = mock(CategoryService::class.java)
        val expected = listOf(CategoryDetails(1, "Milk", BigDecimal("12.34")))
        `when`(service.getCategoryDetails(1, 0)).thenReturn(expected)

        val controller = CategoryController(service)
        val response = controller.getCategoryDetails(1, 0)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }

    @Test
    fun `should return category summary by id`() {
        val service = mock(CategoryService::class.java)
        val expected = CategorySummary(1, "Food", BigDecimal.ONE, BigDecimal.TEN)
        `when`(service.getCategory(1, 0)).thenReturn(expected)

        val controller = CategoryController(service)
        val response = controller.getCategory(1, 0)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }

    @Test
    fun `should use default month for category details`() {
        val service = mock(CategoryService::class.java)
        val expected = listOf(CategoryDetails(1, "Milk", BigDecimal("12.34")))
        `when`(service.getCategoryDetails(1, 0)).thenReturn(expected)

        val controller = CategoryController(service)
        val response = controller.getCategoryDetails(1)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }
}
