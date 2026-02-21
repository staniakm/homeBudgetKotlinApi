package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.CategorySummary
import com.example.demo.entity.CategoryDetails
import io.kotest.matchers.collections.shouldContainAll
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
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")
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
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 1, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("10.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

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
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

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
    fun `should return list fo categories skipping categories without outcome in selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

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

    @Test
    fun `should return empty response when category by id for selected month does not exist`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

        val allCategories = restTemplate.getForEntity("/api/category/4?month=-1", CategorySummary::class.java)

        allCategories.statusCode.is2xxSuccessful
        allCategories.body shouldBe null
    }

    @Test
    fun `should return category summary response for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

        val allCategories = restTemplate.getForEntity("/api/category/1?month=-1", CategorySummary::class.java)

        allCategories.statusCode.is2xxSuccessful
        allCategories.body!!.id shouldBe 1
        allCategories.body!!.name shouldBe "category1"
        allCategories.body!!.monthSummary shouldBe "10.10".toBigDecimal()
        allCategories.body!!.yearSummary shouldBe "30.20".toBigDecimal()
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

    @Test
    fun `should get category details for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createShop()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal.TEN)
        createCategory(1, "category1")
        createCategory(2, "category2")
        createCategory(3, "category3")

        createAssortment(1, "assortment1", 1)
        createAssortment(2, "assortment2", 2)
        createAssortment(3, "assortment2", 3)

        createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

        createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)

        val allCategories = restTemplate.getForEntity(
            "/api/category/1/details?month=-1",
            Array<CategoryDetails>::class.java
        )

        allCategories.statusCode.is2xxSuccessful
        allCategories.body!!.size shouldBe 1
        with(allCategories.body!!.toList()) {
            this shouldContainAll listOf(
                    CategoryDetails(1,"assortment1", "10.10".toBigDecimal()),
            )
        }
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

        val allCategories = restTemplate.getForEntity(
            "/api/category/2/details?month=-1",
            Array<CategoryDetails>::class.java
        )

        allCategories.statusCode.is2xxSuccessful
        allCategories.body!!.size shouldBe 2
        with(allCategories.body!!.asList()) {
            this shouldContainAll listOf(
                    CategoryDetails(2, "assortment2", BigDecimal("10.00")),
                    CategoryDetails(3,"assortment3", BigDecimal("120.00"))
            )
        }

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

}
