package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.seedCategoryBase
import com.example.demo.seedCategoryInvoicesForAprilAndMay
import com.example.demo.seedThreeCategoriesWithAssortments
import com.example.demo.entity.CategorySummary
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class CategoryControllerByIdTest : IntegrationTest() {

    @Test
    fun `should return empty response when category by id for selected month does not exist`() {
        seedCategoryData()

        val category = restTemplate.getForEntity("/api/category/4?month=-1", CategorySummary::class.java)

        category.statusCode.is2xxSuccessful
        category.body shouldBe null
    }

    @Test
    fun `should return category summary response for selected month`() {
        seedCategoryData()

        val category = restTemplate.getForEntity("/api/category/1?month=-1", CategorySummary::class.java)

        category.statusCode.is2xxSuccessful
        category.body!!.id shouldBe 1
        category.body!!.name shouldBe "category1"
        category.body!!.monthSummary shouldBe "10.10".toBigDecimal()
        category.body!!.yearSummary shouldBe "30.20".toBigDecimal()
    }

    @Test
    fun `should return category summary response for default month parameter`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createAssortment(1, "assortment1", 1)
        createInvoice(1, 1, LocalDate.of(2022, 5, 1), BigDecimal("10.10"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)

        val category = restTemplate.getForEntity("/api/category/1", CategorySummary::class.java)

        category.statusCode.is2xxSuccessful
        category.body!!.id shouldBe 1
        category.body!!.monthSummary shouldBe "10.10".toBigDecimal()
    }

    private fun seedCategoryData() {
        seedCategoryBase()
        seedThreeCategoriesWithAssortments()
        seedCategoryInvoicesForAprilAndMay()
    }
}
