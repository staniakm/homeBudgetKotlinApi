package com.example.demo.service

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import com.example.demo.repository.AccountRepository
import com.example.demo.repository.InvoiceRepository
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceServiceTest(
    @Autowired private val invoiceService: InvoiceService,
    @Autowired private val invoiceRepository: InvoiceRepository,
    @Autowired private val accountRepository: AccountRepository
) : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
        createAccountOwner()
        createAccount()
        createShop()
    }

    @Test
    fun `should create new invoice with item`() {
        //given input data
        createCategory(1, "")
        createAssortment(1, "", 1)
        createAssortment(2, "aso2", 1)
        createShopItem(1, 1)
        createShopItem(1, 2)
        val request = NewInvoiceRequest(
            1, 1, LocalDate.now(), listOf(
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
        //when
        val invoice: Invoice? = invoiceService.createNewInvoiceWithItems(request)
        //then
        invoice?.id shouldBe 1
        invoice?.sum shouldBe BigDecimal("21.00")
        invoice?.del shouldBe false
        //and
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
        //expect
        val invoice = invoiceRepository.getInvoice(1)
        invoice shouldBe null
        //when delete invoice
        val invoiceId: Long? = invoiceService.deleteInvoice(1)

        //then should return id
        invoiceId shouldBe null
    }

    @Test
    fun `should delete existing invoice`() {
        //given prepare initial data
        createInvoice(1)
        //expect invoice exists in database
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        //when delete invoice
        val invoiceId: Long? = invoiceService.deleteInvoice(1)

        //then should return id
        invoiceId shouldBe 1
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
    }

    @Test
    fun `should delete existing invoice with details`() {
        //given prepare initial data
        createCategory(1, "")
        createAssortment(1, "", 1)
        createShopItem(1, 1)
        createInvoice(1)
        createInvoiceItem(1, 1, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, 1, 1)
        //expect invoice exists in database
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        //expecet invoice details exists in database
        val itemsCount = invoiceRepository.getInvoiceDetails(1)
        itemsCount.size shouldBe 1
        //when delete invoice
        val invoiceId: Long? = invoiceService.deleteInvoice(1)

        //then should return id
        invoiceId shouldBe 1
        //and invoice should be deleted
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
        //and invoice items should be deleted
        invoiceRepository.getInvoiceDetails(1) shouldBe emptyList()
    }


    @Test
    fun `should delete existing invoice with details and update account money amount`() {
        //given prepare initial data
        createCategory(1, "")
        createAssortment(1, "", 1)
        createShopItem(1, 1)
        createInvoice(1)
        createInvoiceItem(1, 1, BigDecimal("10.0"), BigDecimal("2"), BigDecimal("5"), BigDecimal.ZERO, 1, 1)
        //expect invoice exists in database
        val existingInvoice: Invoice? = invoiceRepository.getInvoice(1)
        existingInvoice?.id shouldBe 1
        //expected invoice details exists in database
        val itemsCount = invoiceRepository.getInvoiceDetails(1)
        itemsCount.size shouldBe 1
        //when delete invoice
        val invoiceId: Long? = invoiceService.deleteInvoice(1)
        //then should return id
        invoiceId shouldBe 1
        //and invoice should be deleted
        val deletedInvoice: Invoice? = invoiceRepository.getInvoice(1)
        deletedInvoice shouldBe null
        //and account money amount should be updated
        val account = accountRepository.findById(1)
        account?.amount?.shouldBeEqualComparingTo(BigDecimal("11.0"))
    }

}