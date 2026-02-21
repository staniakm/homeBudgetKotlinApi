package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Invoice
import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.ShoppingInvoice
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceRepositoryTest(
    @Autowired private val invoiceRepository: InvoiceRepository,
) : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
        createAccountOwner()
        createAccount()
        createShop()
    }

    @Test
    fun `should fetch invoices for selected month`() {
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))
        createInvoice(invoiceId = 2, date = LocalDate.of(2021, 11, 2), amount = BigDecimal("20.02"))
        createInvoice(invoiceId = 3, date = LocalDate.of(2021, 11, 3), amount = BigDecimal("30.03"))
        createInvoice(invoiceId = 4, date = LocalDate.of(2021, 12, 3))

        val invoices = invoiceRepository.getInvoicesForMonth(LocalDate.of(2021, 11, 30))

        invoices.size shouldBe 3
        invoices shouldContainAll listOf(
            ShoppingInvoice(1, "ShopName", LocalDate.of(2021, 11, 1), BigDecimal("10.01"), "account"),
            ShoppingInvoice(2, "ShopName", LocalDate.of(2021, 11, 2), BigDecimal("20.02"), "account"),
            ShoppingInvoice(3, "ShopName", LocalDate.of(2021, 11, 3), BigDecimal("30.03"), "account")
        )
    }

    @Test
    fun `should fetch invoice details`() {
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))
        createCategory(1, "cat1")
        createCategory(2, "cat2")
        createCategory(3, "cat3")
        createAssortment(1, "aso1", 1)
        createAssortment(2, "aso2", 2)
        createAssortment(3, "aso3", 3)
        createInvoiceItem(1, 1, BigDecimal("1.01"), BigDecimal("1.00"), "1.01".toBigDecimal(), "0.00".toBigDecimal(), 1, 1)
        createInvoiceItem(2, 1, BigDecimal("2.01"), BigDecimal("2.00"), "1.02".toBigDecimal(), "0.00".toBigDecimal(), 2, 2)
        createInvoiceItem(3, 1, BigDecimal("3.01"), BigDecimal("3.00"), "1.03".toBigDecimal(), "1.10".toBigDecimal(), 3, 3)

        val invoiceDetails = invoiceRepository.getInvoiceDetails(1)

        invoiceDetails.size shouldBe 3
        invoiceDetails shouldContain InvoiceItem(1, "aso1", "1.000".toBigDecimal(), "1.01".toBigDecimal(), "0.00".toBigDecimal(), "1.01".toBigDecimal(), 1)
        invoiceDetails shouldContain InvoiceItem(2, "aso2", "2.000".toBigDecimal(), "1.02".toBigDecimal(), "0.00".toBigDecimal(), "2.01".toBigDecimal(), 2)
        invoiceDetails shouldContain InvoiceItem(3, "aso3", "3.000".toBigDecimal(), "1.03".toBigDecimal(), "1.10".toBigDecimal(), "3.01".toBigDecimal(), 3)
    }

    @Test
    fun `should fetch selected invoice`() {
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))

        val invoice = invoiceRepository.getInvoice(1)

        invoice shouldBe Invoice(1, LocalDate.of(2021, 11, 1), "1a", "10.01".toBigDecimal(), "", false, 1, 1)
    }

    @Test
    fun `should fetch account invoice for month`() {
        createAccount(2, name = "account 2")
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))
        createInvoice(invoiceId = 2, date = LocalDate.of(2021, 11, 2), amount = BigDecimal("20.02"))
        createInvoice(invoiceId = 3, date = LocalDate.of(2021, 11, 2), amount = BigDecimal("20.02"), accountId = 2)

        val invoices = invoiceRepository.getAccountInvoices(1, LocalDate.of(2021, 11, 1))

        invoices.size shouldBe 2
        invoices shouldContainAll listOf(
            ShoppingInvoice(1, "ShopName", LocalDate.of(2021, 11, 1), BigDecimal("10.01"), "account"),
            ShoppingInvoice(2, "ShopName", LocalDate.of(2021, 11, 2), BigDecimal("20.02"), "account"),
        )
    }

    @Test
    fun `should change invoice account`() {
        createAccount(2, name = "account 2", amount = "100.00".toBigDecimal())
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.00"))

        invoiceRepository.updateInvoiceAccount(1, 2)
        val invoice = invoiceRepository.getInvoice(1)

        invoice?.account shouldBe 2
    }
}
