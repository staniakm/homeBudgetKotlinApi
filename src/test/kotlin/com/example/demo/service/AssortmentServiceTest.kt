package com.example.demo.service

import com.example.demo.entity.Assortment
import com.example.demo.entity.Category
import com.example.demo.repository.AssortmentRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@Tag("unit")
class AssortmentServiceTest {

    @Test
    fun `should return same assortment when category is unchanged`() {
        val repository = mock(AssortmentRepository::class.java)
        val categoryService = mock(CategoryService::class.java)
        val assortment = Assortment(1, "Milk", 2)
        `when`(repository.getAssortmentById(1)).thenReturn(assortment)

        val service = AssortmentService(repository, categoryService)
        val result = service.changeAssortmentCategory(1, 2)

        result shouldBe assortment
    }

    @Test
    fun `should return null when assortment does not exist`() {
        val repository = mock(AssortmentRepository::class.java)
        val categoryService = mock(CategoryService::class.java)
        `when`(repository.getAssortmentById(99)).thenReturn(null)

        val service = AssortmentService(repository, categoryService)
        val result = service.changeAssortmentCategory(99, 2)

        result shouldBe null
    }

    @Test
    fun `should update and return assortment when destination category exists`() {
        val repository = mock(AssortmentRepository::class.java)
        val categoryService = mock(CategoryService::class.java)
        val initial = Assortment(1, "Milk", 1)
        val updated = Assortment(1, "Milk", 2)
        `when`(repository.getAssortmentById(1)).thenReturn(initial, updated)
        `when`(categoryService.getCategoryById(2)).thenReturn(Category(2, "Home"))

        val service = AssortmentService(repository, categoryService)
        val result = service.changeAssortmentCategory(1, 2)

        result shouldBe updated
    }
}
