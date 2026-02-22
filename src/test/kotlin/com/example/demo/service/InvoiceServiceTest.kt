package com.example.demo.service

import com.example.demo.IntegrationTest
import com.example.demo.CatalogSeedItem
import com.example.demo.entity.*
import com.example.demo.repository.AccountRepository
import com.example.demo.repository.InvoiceRepository
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class InvoiceServiceTest(
    @Autowired private val invoiceService: InvoiceService,
    @Autowired private val invoiceRepository: InvoiceRepository,
    @Autowired private val accountRepository: AccountRepository
) : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
        testDataBuilder.givenDefaultFinanceContext()
    }

    @Test
    fun `should create new invoice with item`() {
        testDataBuilder.givenCatalog(
            CatalogSeedItem(1, "", 1, ""),
            CatalogSeedItem(2, "aso2", 1, "")
        )
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)
        val request = NewInvoiceRequest(
            1, 1, clockProvider.getDate(), listOf(
                NewInvoiceItemRequest(
                    ShopItem(1, "item"), BigDecimal.ONE,
                    BigDecimal.ONE, BigDecimal("0.20"), BigDecimal.ONE
                ),
                NewInvoiceItemRequest(
                    ShopItem(2, "item2"), BigDecimal("10.1"),
                    BigDecimal("2"), BigDecimal.ZERO, BigDecimal("20.2")
                )
            ), BigDecimal.ONE, "", ""
        )
        val invoice: Invoice? = invoiceService.createNewInvoiceWithItems(request)
        invoice?.id shouldBe 1
        invoice?.sum shouldBe BigDecimal("21.00")
        invoice?.del shouldBe false
        val invoiceDetails: List<InvoiceItem> = invoiceService.getInvoiceDetails(1)
        invoiceDetails.size shouldBe 2
        invoiceDetails.map { it.itemId } shouldBe listOf(1, 2)
        invoiceDetails.map { it.price } shouldBe listOf(BigDecimal("1.00"), BigDecimal("10.10"))
        invoiceDetails.map { it.totalPrice } shouldBe listOf(BigDecimal("0.80"), BigDecimal("20.20"))
        val account = accountRepository.findById(1)
        account?.amount?.shouldBeEqualComparingTo(BigDecimal("-20.00"))
    }

    @Test
    fun `should return null when no invoice exists`() {
        val invoice = invoiceRepository.getInvoice(1)
        invoice shouldBe null
        val invoiceId: Long? = invoiceService.deleteInvoice(1)
        invoiceId shouldBe null
    }

    @Test
    fun `should delete existing invoice`() {
        testDataBuilder.invoice(1)
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        val invoiceId: Long? = invoiceService.deleteInvoice(1)
        invoiceId shouldBe 1
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
    }

    @Test
    fun `should delete existing invoice with details`() {
        testDataBuilder.category(1, "")
        testDataBuilder.assortment(1, "", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.invoice(1)
        testDataBuilder.invoiceItem(1, 1, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, 1, 1)
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        val itemsCount = invoiceRepository.getInvoiceDetails(1)
        itemsCount.size shouldBe 1
        val invoiceId: Long? = invoiceService.deleteInvoice(1)
        invoiceId shouldBe 1
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
        invoiceRepository.getInvoiceDetails(1) shouldBe emptyList()
    }


    @Test
    fun `should delete existing invoice with details and update account money amount`() {
        testDataBuilder.category(1, "")
        testDataBuilder.assortment(1, "", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.invoice(1)
        testDataBuilder.invoiceItem(1, 1, BigDecimal("10.0"), BigDecimal("2"), BigDecimal("5"), BigDecimal.ZERO, 1, 1)
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        val itemsCount = invoiceRepository.getInvoiceDetails(1)
        itemsCount.size shouldBe 1
        val invoiceId: Long? = invoiceService.deleteInvoice(1)
        invoiceId shouldBe 1
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
        val account = accountRepository.findById(1)
        account?.amount?.shouldBeEqualComparingTo(BigDecimal("11.0"))
    }

}
