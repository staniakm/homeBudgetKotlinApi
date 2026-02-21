package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.seedCategoryBase
import com.example.demo.seedCategoryInvoicesForAprilAndMay
import com.example.demo.seedThreeCategoriesWithAssortments
import com.example.demo.entity.CategoryDetails
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class CategoryControllerDetailsTest : IntegrationTest() {

    @Test
    fun `should get category details for selected month`() {
        seedCategoryDataForDetails()

        val details = restTemplate.getForEntity("/api/category/1/details?month=-1", Array<CategoryDetails>::class.java)

        details.statusCode.is2xxSuccessful
        details.body!!.size shouldBe 1
        details.body!!.toList() shouldContainAll listOf(CategoryDetails(1, "assortment1", "10.10".toBigDecimal()))
    }

    @Test
    fun `should get list of category details for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment3", 2)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 2, 3)

        createInvoice(2, 1, LocalDate.of(2022, 4, 5), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)
        createInvoiceItem(5, 2, BigDecimal("20.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 3)

        val details = restTemplate.getForEntity("/api/category/2/details?month=-1", Array<CategoryDetails>::class.java)

        details.statusCode.is2xxSuccessful
        details.body!!.size shouldBe 2
        details.body!!.asList() shouldContainAll listOf(
            CategoryDetails(2, "assortment2", BigDecimal("10.00")),
            CategoryDetails(3, "assortment3", BigDecimal("120.00"))
        )
    }

    @Test
    fun `should get category details for default month parameter`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createAssortment(1, "assortment1", 1)
        createInvoice(1, 1, LocalDate.of(2022, 5, 5), BigDecimal("10.10"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)

        val details = restTemplate.getForEntity("/api/category/1/details", Array<CategoryDetails>::class.java)

        details.statusCode.is2xxSuccessful
        details.body!!.size shouldBe 1
        details.body!!.first().assortmentId shouldBe 1
    }

    private fun seedCategoryDataForDetails() {
        seedCategoryBase()
        seedThreeCategoriesWithAssortments()
        seedCategoryInvoicesForAprilAndMay()
    }
}
