package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceRepositoryTest(@Autowired private val invoiceRepository: InvoiceRepository) : IntegrationTest() {

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

        val invoices = invoiceRepository.getInvoicesForMonth(LocalDate.of(2021, 11, 30)).collectList().block()!!

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
        createInvoiceItem(
            1,
            1,
            BigDecimal("1.01"),
            BigDecimal("1.00"),
            "1.01".toBigDecimal(),
            "0.00".toBigDecimal(),
            1,
            1
        )
        createInvoiceItem(
            2,
            1,
            BigDecimal("2.01"),
            BigDecimal("2.00"),
            "1.02".toBigDecimal(),
            "0.00".toBigDecimal(),
            2,
            2
        )
        createInvoiceItem(
            3,
            1,
            BigDecimal("3.01"),
            BigDecimal("3.00"),
            "1.03".toBigDecimal(),
            "1.10".toBigDecimal(),
            3,
            3
        )

        val invoiceDetails = invoiceRepository.getInvoiceDetails(1).collectList().block()!!

        invoiceDetails.size shouldBe 3
        invoiceDetails shouldContain ShopCartDetails(
            1,
            "aso1",
            "1.000".toBigDecimal(),
            "1.01".toBigDecimal(),
            "0.00".toBigDecimal(),
            "1.01".toBigDecimal(),
            1
        )
        invoiceDetails shouldContain ShopCartDetails(
            2,
            "aso2",
            "2.000".toBigDecimal(),
            "1.02".toBigDecimal(),
            "0.00".toBigDecimal(),
            "2.01".toBigDecimal(),
            2
        )
        invoiceDetails shouldContain ShopCartDetails(
            3,
            "aso3",
            "3.000".toBigDecimal(),
            "1.03".toBigDecimal(),
            "1.10".toBigDecimal(),
            "3.01".toBigDecimal(),
            3
        )
    }

    @Test
    fun `should fetch selected invoice`() {
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))

        val invoice = invoiceRepository.getInvoice(1).block()!!

        invoice shouldBe Invoice(1, LocalDate.of(2021, 11, 1), "1a", "10.01".toBigDecimal(), "", false, 1, 1)
    }

    @Test
    fun `should fetch account invoice for month`() {

        createAccount(2, name = "account 2")
        createInvoice(invoiceId = 1, date = LocalDate.of(2021, 11, 1), amount = BigDecimal("10.01"))
        createInvoice(invoiceId = 2, date = LocalDate.of(2021, 11, 2), amount = BigDecimal("20.02"))
        createInvoice(invoiceId = 3, date = LocalDate.of(2021, 11, 2), amount = BigDecimal("20.02"), accountId = 2)

        val invoices = invoiceRepository.getAccountInvoices(1, LocalDate.of(2021, 11, 1)).collectList().block()!!

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

        invoiceRepository.updateInvoiceAccount(1, 2).block()
        val invoice = invoiceRepository.getInvoice(1).block()!!

        invoice.account shouldBe 2
    }

    @Test
    fun `should create new invoice`() {

        val request = NewInvoiceRequest(
            1, 1, LocalDate.of(2022, 1, 1), listOf(), BigDecimal.TEN, "invoiceNumber", ""
        )
        invoiceRepository.createInvoice(request).block()

        val invoices = invoiceRepository.getLastInsertedInvoice().block()!!

        invoices shouldBe Invoice(1, LocalDate.of(2022, 1, 1), "invoiceNumber", BigDecimal("10.00"), "", false, 1, 1)
    }

    @Test
    fun `should create invoice items`() {
        createCategory()
        createAssortment(1, "item1", 1)
        createAssortment(2, "item2", 1)
        createInvoice()
        val item1 = NewInvoiceItemRequest(
            ShopItem(1, "item1"),
            BigDecimal(10),
            BigDecimal("0.5"),
            BigDecimal.ZERO,
            BigDecimal("5")
        )
        val item2 = NewInvoiceItemRequest(
            ShopItem(2, "item2"),
            BigDecimal(10),
            BigDecimal("0.5"),
            BigDecimal.ZERO,
            BigDecimal("5")
        )

        val ids = invoiceRepository.createInvoiceItems(1, listOf(item1, item2)).collectList().block()!!

        ids shouldContainAll listOf(1, 2)

        val items = invoiceRepository.getInvoiceDetails(1).collectList().block()!!
        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopCartDetails(
                1,
                "item1",
                BigDecimal("0.500"),
                BigDecimal("10.00"),
                BigDecimal("0.00"),
                BigDecimal("5.00"),
                1
            ),
            ShopCartDetails(
                2,
                "item2",
                BigDecimal("0.500"),
                BigDecimal("10.00"),
                BigDecimal("0.00"),
                BigDecimal("5.00"),
                2
            )
        )
    }

    @Test
    fun `should recalculate invoice`() {
        createCategory(1, "Cat1")
        createCategory(2, "Cat2")
        createAssortment(1, "aso1", 2)
        createInvoice(amount = BigDecimal.ZERO)
        createInvoiceItem(1, 1, BigDecimal("10.00"), BigDecimal("2"), BigDecimal("10.00"), BigDecimal.ONE, 1, 1)
        createInvoiceItem(2, 1, BigDecimal("20.00"), BigDecimal("1"), BigDecimal("12.00"), BigDecimal.ONE, 1, 1)

        invoiceRepository.recaculatInvoice(1).block()

        val invoice = invoiceRepository.getInvoice(1).block()!!
        invoice.sum shouldBe BigDecimal("30.00")

        val items = invoiceRepository.getInvoiceDetails(1).collectList().block()!!
        items.size shouldBe 2
        items.map { it.totalPrice } shouldContainAll listOf(BigDecimal("19.00"), BigDecimal("11.00"))

    }
}