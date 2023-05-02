package com.example.demo.repository

import com.example.demo.IntegrationTest
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class ProductRepositoryTest(@Autowired private val productRepository: ProductRepository) : IntegrationTest() {

    @Test
    fun `should fetch selected item history`() {
        createAccountOwner()
        createAccount()
        createShop()
        createCategory(1)
        createAssortment(1, "Aso", 1)
        createAssortment(2, "Aso2", 1)
        createInvoice(1)
        createInvoice(2)
        createInvoice(3)
        createInvoiceItem(1, 1, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(2, 1, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 2)
        createInvoiceItem(3, 2, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 2)
        createInvoiceItem(4, 2, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(5, 3, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        createInvoiceItem(6, 3, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)

        val history = productRepository.getProductHistory(1)

        history.size shouldBe 4
        history.map { it.invoiceDetailsId } shouldContainAll listOf(1, 4, 5, 6)

    }

    @Test
    fun `should fetch assortment by id`() {
        createAccountOwner()
        createAccount()
        createShop()
        createCategory(1, "CATEGORY_1")
        createCategory(2, "CATEGORY_2")
        createAssortment(1, "Aso", 1)
        createAssortment(2, "Aso2", 2)

        val assortment = productRepository.getProduct(2)

        assortment?.categoryId shouldBe 2
        assortment?.categoryName shouldBe "CATEGORY_2"
        assortment?.name shouldBe "Aso2"
    }

    @Test
    fun `should fetch null when product not exists`() {
        createAccountOwner()
        createAccount()
        createShop()
        createCategory(1, "CATEGORY_1")
        createCategory(2, "CATEGORY_2")
        createAssortment(1, "Aso", 1)
        createAssortment(2, "Aso2", 2)

        val assortment = productRepository.getProduct(3)

        assortment shouldBe null
    }

    @Test
    fun `should update category`() {
        createAccountOwner()
        createAccount()
        createShop()
        createCategory(1)
        createCategory(2, "CATEGORY_2")
        createAssortment(1, "Aso", 1)
        createAssortment(2, "Aso2", 1)

        productRepository.updateCategory(1, 2)

        val history = productRepository.getProduct(1)

        history?.categoryId shouldBe 2
        history?.categoryName shouldBe "CATEGORY_2"
        history?.name shouldBe "Aso"

    }
}