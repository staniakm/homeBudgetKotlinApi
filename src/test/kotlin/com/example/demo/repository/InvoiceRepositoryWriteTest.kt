package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.CatalogSeedItem
import com.example.demo.InvoiceItemSeed
import com.example.demo.entity.*
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceRepositoryWriteTest(
    @Autowired private val invoiceRepository: InvoiceRepository,
    @Autowired private val accountRepository: AccountRepository
) : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
        testDataBuilder.givenDefaultFinanceContext()
    }

    @Test
    fun `should create new invoice`() {
        val request = NewInvoiceRequest(1, 1, LocalDate.of(2022, 1, 1), listOf(), BigDecimal.TEN, "invoiceNumber", "")
        val invoice = invoiceRepository.createInvoice(request)

        invoice shouldBe Invoice(1, LocalDate.of(2022, 1, 1), "invoiceNumber", BigDecimal("10.00"), "", false, 1, 1)
    }

    @Test
    fun `should create invoice items`() {
        testDataBuilder.category()
        testDataBuilder.assortment(1, "item1", 1)
        testDataBuilder.assortment(2, "item2", 1)
        testDataBuilder.invoice()
        val item1 = NewInvoiceItemRequest(ShopItem(1, "item1"), BigDecimal(10), BigDecimal("0.5"), BigDecimal.ZERO, BigDecimal("5"))
        val item2 = NewInvoiceItemRequest(ShopItem(2, "item2"), BigDecimal(10), BigDecimal("0.5"), BigDecimal.ZERO, BigDecimal("5"))

        invoiceRepository.createInvoiceItems(1, listOf(item1, item2))

        val items = invoiceRepository.getInvoiceDetails(1)
        items.size shouldBe 2
        items shouldContainAll listOf(
            InvoiceItem(1, "item1", BigDecimal("0.500"), BigDecimal("10.00"), BigDecimal("0.00"), BigDecimal("5.00"), 1),
            InvoiceItem(2, "item2", BigDecimal("0.500"), BigDecimal("10.00"), BigDecimal("0.00"), BigDecimal("5.00"), 2)
        )
    }

    @Test
    fun `should recalculate invoice`() {
        testDataBuilder.category(1, "Cat1")
        testDataBuilder.givenCatalog(CatalogSeedItem(1, "aso1", 2, "Cat2"))
        testDataBuilder.givenInvoiceWithItems(
            items = listOf(
                InvoiceItemSeed(1, BigDecimal("10.00"), BigDecimal("2"), BigDecimal("10.00"), BigDecimal.ONE, 1, 1),
                InvoiceItemSeed(2, BigDecimal("20.00"), BigDecimal("1"), BigDecimal("12.00"), BigDecimal.ONE, 1, 1)
            )
        )

        invoiceRepository.recaculatInvoice(1)

        val invoice = invoiceRepository.getInvoice(1)
        invoice?.sum shouldBe BigDecimal("30.00")

        val items = invoiceRepository.getInvoiceDetails(1)
        items.size shouldBe 2
        items.map { it.totalPrice } shouldContainAll listOf(BigDecimal("19.00"), BigDecimal("11.00"))
    }

    @Test
    fun `should create automatic invoice with zero sum when no automatic entries exists`() {
        testDataBuilder.account(accountId = 3)
        testDataBuilder.shop(shopId = 8)

        invoiceRepository.createAutoInvoice()

        val invoice = invoiceRepository.getInvoice(1)
        invoice shouldNotBe null
        invoice?.let {
            it.sum shouldBeEqualComparingTo BigDecimal.ZERO
            it.account shouldBe 3
            it.shop shouldBe 8
            it.invoiceNumber shouldStartWith "Rachunki"
        }
    }

    @Test
    fun `should create automatic invoice`() {
        testDataBuilder.account(accountId = 3, amount = BigDecimal("100"))
        testDataBuilder.shop(shopId = 8)
        testDataBuilder.category(2, "Cat2")
        testDataBuilder.assortment(1, "aso1", 2)
        testDataBuilder.assortment(2, "aso2", 2)
        testDataBuilder.autoinvoiceEntry(asoId = 1, price = BigDecimal("1.2"), quantity = BigDecimal.ONE)
        testDataBuilder.autoinvoiceEntry(asoId = 2, price = BigDecimal("8.9"), quantity = BigDecimal.ONE)

        invoiceRepository.createAutoInvoice()

        val invoice = invoiceRepository.getInvoice(1)
        invoice shouldNotBe null
        invoice?.let {
            it.sum shouldBeEqualComparingTo BigDecimal("10.1")
            it.account shouldBe 3
            it.shop shouldBe 8
            it.invoiceNumber shouldStartWith "Rachunki"
        }
        val account = accountRepository.findById(3)
        account?.amount?.shouldBeEqualComparingTo(BigDecimal("89.9"))
    }

    @Test
    fun `should create batch invoice details`() {
        testDataBuilder.category(1, "Cat1")
        testDataBuilder.givenCatalog(
            CatalogSeedItem(1, "aso1", 2, "Cat2"),
            CatalogSeedItem(2, "aso2", 2, "Cat2")
        )
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)
        testDataBuilder.invoice(invoiceId = 10, amount = BigDecimal.ZERO)

        invoiceRepository.createInvoiceItems(
            10,
            listOf(
                NewInvoiceItemRequest(ShopItem(1, "aso1"), BigDecimal("10.00"), BigDecimal("0.5"), BigDecimal.ZERO, BigDecimal("5.00")),
                NewInvoiceItemRequest(ShopItem(2, "aso2"), unitPrice = BigDecimal("10.00"), amount = BigDecimal("1.5"), discount = BigDecimal.ZERO, totalPrice = BigDecimal("15.00"))
            )
        )

        val invoiceDetails = invoiceRepository.getInvoiceDetails(10)
        invoiceDetails.size shouldBe 2
        invoiceDetails shouldContainAll listOf(
            InvoiceItem(1, "aso1", BigDecimal("0.500"), BigDecimal("10.00"), BigDecimal("0.00"), BigDecimal("5.00"), 1),
            InvoiceItem(2, "aso2", BigDecimal("1.500"), BigDecimal("10.00"), BigDecimal("0.00"), BigDecimal("15.00"), 2)
        )
    }
}
