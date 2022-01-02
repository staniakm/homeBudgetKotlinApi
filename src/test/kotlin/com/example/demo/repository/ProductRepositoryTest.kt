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

        val history = productRepository.getProductHistory(1).collectList().block()!!

        history.size shouldBe 4
        history.map { it.invoiceDetailsId } shouldContainAll listOf(1, 4, 5, 6)

    }
}