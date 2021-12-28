package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class CategoryRepositoryTest(@Autowired private val categoryRepository: CategoryRepository) : IntegrationTest() {


    @Test
    fun `should fetch categories summary for month and year`() {
        createAccountOwner()
        createAccount()
        createCategory(1, "category 1")
        createCategory(2, "category 2")
        createAssortment(1, "assortment 1", 1)
        createShop()
        createInvoice(1, 1, LocalDate.of(2021, 10, 11))
        createInvoice(2, 1, LocalDate.of(2021, 11, 11))
        createInvoice(3, 1, LocalDate.of(2021, 12, 11))
        createInvoice(4, 1, LocalDate.of(2021, 11, 11))

        createInvoiceItem(1, 1, BigDecimal(10), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(2, 1, BigDecimal(20), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 2, 1)
        createInvoiceItem(3, 2, BigDecimal(30), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 2, BigDecimal(40), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 2, 1)
        createInvoiceItem(5, 3, BigDecimal(50), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(6, 4, BigDecimal(60), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)


        val data = categoryRepository.getCategoriesSummary(LocalDate.of(2021, 11, 1)).collectList().block()!!

        data.size shouldBe 2
        data shouldContain Category(1, "category 1", BigDecimal("90.00"), BigDecimal("150.00"))
        data shouldContain Category(2, "category 2", BigDecimal("40.00"), BigDecimal("60.00"))
    }

    @Test
    fun `should fetch single category summary`() {
        createAccountOwner()
        createAccount()
        createCategory(1, "category 1")
        createCategory(2, "category 2")
        createAssortment(1, "assortment 1", 1)
        createShop()
        createInvoice(1, 1, LocalDate.of(2021, 10, 11))
        createInvoice(2, 1, LocalDate.of(2021, 11, 11))
        createInvoice(3, 1, LocalDate.of(2021, 12, 11))
        createInvoice(4, 1, LocalDate.of(2021, 11, 11))

        createInvoiceItem(1, 1, BigDecimal(10), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(2, 1, BigDecimal(20), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 2, 1)
        createInvoiceItem(3, 2, BigDecimal(30), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 2, BigDecimal(40), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 2, 1)
        createInvoiceItem(5, 3, BigDecimal(50), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(6, 4, BigDecimal(60), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)


        val data = categoryRepository.getCategory(1, LocalDate.of(2021, 11, 1)).block()!!

        data shouldBe Category(1, "category 1", BigDecimal("90.00"), BigDecimal("150.00"))
    }

    @Test
    fun `should fetch single category details`() {
        createAccountOwner()
        createAccount()
        createCategory(1, "category 1")
        createCategory(2, "category 2")
        createAssortment(1, "assortment 1", 1)
        createAssortment(2, "assortment 2", 1)
        createShop()
        createInvoice(1, 1, LocalDate.of(2021, 10, 11))
        createInvoice(2, 1, LocalDate.of(2021, 11, 11))
        createInvoice(3, 1, LocalDate.of(2021, 12, 11))
        createInvoice(4, 1, LocalDate.of(2021, 11, 11))

        createInvoiceItem(1, 1, BigDecimal(10), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(2, 1, BigDecimal(20), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 2, 1)
        createInvoiceItem(3, 2, BigDecimal(30), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(4, 2, BigDecimal(40), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 2)
        createInvoiceItem(5, 3, BigDecimal(50), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(6, 4, BigDecimal(60), BigDecimal(1), BigDecimal(1), BigDecimal.ZERO, 1, 2)


        val data = categoryRepository.getProductsForCategoryAndMonth(1, LocalDate.of(2021, 11, 1)).collectList().block()!!

        data.size shouldBe 2
        data shouldContain CategoryDetails("assortment 1", BigDecimal("30.00"))
        data shouldContain CategoryDetails("assortment 2", BigDecimal("100.00"))
    }
}