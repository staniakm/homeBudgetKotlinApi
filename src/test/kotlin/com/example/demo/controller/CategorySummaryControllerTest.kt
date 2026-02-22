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

class CategorySummaryControllerTest : IntegrationTest() {

    @Test
    fun `should return empty list when no categories exists`() {
        val findAllCategories = restTemplate.getForEntity("/api/category", Array<CategorySummary>::class.java)

        findAllCategories.statusCode.is2xxSuccessful
        findAllCategories.body!!.size shouldBe 0
    }

    @Test
    fun `should return list of categories with zero outcome when no invoices exists`() {
        testDataBuilder.category(1, "category1")
        testDataBuilder.category(2, "category2")
        testDataBuilder.category(3, "category3")

        val findAllCategories = restTemplate.getForEntity("/api/category", Array<CategorySummary>::class.java)

        findAllCategories.statusCode.is2xxSuccessful
        findAllCategories.body!!.size shouldBe 3
        with(findAllCategories.body!!.asList()) {
            this[0].id shouldBe 1
            this[0].name shouldBe "category1"
            this[0].monthSummary shouldBe "0.00".toBigDecimal()
            this[0].yearSummary shouldBe "0.00".toBigDecimal()
            this[1].id shouldBe 2
            this[1].name shouldBe "category2"
            this[1].monthSummary shouldBe "0.00".toBigDecimal()
            this[1].yearSummary shouldBe "0.00".toBigDecimal()
            this[2].id shouldBe 3
            this[2].name shouldBe "category3"
            this[2].monthSummary shouldBe "0.00".toBigDecimal()
            this[2].yearSummary shouldBe "0.00".toBigDecimal()
        }
    }

    @Test
    fun `should return list of categories with calculated outcome for default month`() {
        seedCategoryBase()
        seedThreeCategoriesWithAssortments()
        testDataBuilder.invoice(1, 1, LocalDate.of(2022, 1, 1), BigDecimal.TEN, 1)
        testDataBuilder.invoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        testDataBuilder.invoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)
        testDataBuilder.invoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        testDataBuilder.invoiceItem(2, 2, BigDecimal("10.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        testDataBuilder.invoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

        val findAllCategories = restTemplate.getForEntity("/api/category", Array<CategorySummary>::class.java)

        findAllCategories.statusCode.is2xxSuccessful
        findAllCategories.body!!.size shouldBe 3
        with(findAllCategories.body!!.asList()) {
            this[0].id shouldBe 1
            this[0].name shouldBe "category1"
            this[0].monthSummary shouldBe "10.10".toBigDecimal()
            this[0].yearSummary shouldBe "20.20".toBigDecimal()
            this[1].id shouldBe 2
            this[1].name shouldBe "category2"
            this[1].monthSummary shouldBe "10.00".toBigDecimal()
            this[1].yearSummary shouldBe "10.00".toBigDecimal()
            this[2].id shouldBe 3
            this[2].name shouldBe "category3"
            this[2].monthSummary shouldBe "0.00".toBigDecimal()
            this[2].yearSummary shouldBe "100.00".toBigDecimal()
        }
    }

    @Test
    fun `should return list of categories with calculated month summary for selected month`() {
        seedCategoryBase()
        seedThreeCategoriesWithAssortments()
        seedCategoryInvoicesForAprilAndMay()

        val allCategories = restTemplate.getForEntity("/api/category?month=-1", Array<CategorySummary>::class.java)

        allCategories.statusCode.is2xxSuccessful
        allCategories.body!!.size shouldBe 3
        with(allCategories.body!!.asList()) {
            this[0].id shouldBe 1
            this[0].name shouldBe "category1"
            this[0].monthSummary shouldBe "10.10".toBigDecimal()
            this[0].yearSummary shouldBe "30.20".toBigDecimal()
            this[1].id shouldBe 2
            this[1].name shouldBe "category2"
            this[1].monthSummary shouldBe "0.00".toBigDecimal()
            this[1].yearSummary shouldBe "10.00".toBigDecimal()
            this[2].id shouldBe 3
            this[2].name shouldBe "category3"
            this[2].monthSummary shouldBe "100.00".toBigDecimal()
            this[2].yearSummary shouldBe "100.00".toBigDecimal()
        }
    }

    @Test
    fun `should return list of categories skipping categories without outcome in selected month`() {
        seedCategoryBase()
        seedThreeCategoriesWithAssortments()
        seedCategoryInvoicesForAprilAndMay()

        val allCategories = restTemplate.getForEntity(
            "/api/category?month=-1&skipZero=true",
            Array<CategorySummary>::class.java
        )

        allCategories.statusCode.is2xxSuccessful
        allCategories.body!!.size shouldBe 2
        with(allCategories.body!!.asList()) {
            this[0].id shouldBe 1
            this[0].name shouldBe "category1"
            this[0].monthSummary shouldBe "10.10".toBigDecimal()
            this[0].yearSummary shouldBe "30.20".toBigDecimal()
            this[1].id shouldBe 3
            this[1].name shouldBe "category3"
            this[1].monthSummary shouldBe "100.00".toBigDecimal()
            this[1].yearSummary shouldBe "100.00".toBigDecimal()
        }
    }
}
