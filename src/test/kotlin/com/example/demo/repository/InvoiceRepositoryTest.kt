package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import io.kotest.matchers.collections.shouldContain
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

class InvoiceRepositoryTest(
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

        val invoiceDetails = invoiceRepository.getInvoiceDetails(1)

        invoiceDetails.size shouldBe 3
        invoiceDetails shouldContain InvoiceItem(
            1,
            "aso1",
            "1.000".toBigDecimal(),
            "1.01".toBigDecimal(),
            "0.00".toBigDecimal(),
            "1.01".toBigDecimal(),
            1
        )
        invoiceDetails shouldContain InvoiceItem(
            2,
            "aso2",
            "2.000".toBigDecimal(),
            "1.02".toBigDecimal(),
            "0.00".toBigDecimal(),
            "2.01".toBigDecimal(),
            2
        )
        invoiceDetails shouldContain InvoiceItem(
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

    @Test
    fun `should create new invoice`() {

        val request = NewInvoiceRequest(
            1, 1, LocalDate.of(2022, 1, 1), listOf(), BigDecimal.TEN, "invoiceNumber", ""
        )
        val invoice = invoiceRepository.createInvoice(request)

        invoice shouldBe Invoice(1, LocalDate.of(2022, 1, 1), "invoiceNumber", BigDecimal("10.00"), "", false, 1, 1)
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

        invoiceRepository.createInvoiceItems(1, listOf(item1, item2))

        //then
        val items = invoiceRepository.getInvoiceDetails(1)
        items.size shouldBe 2
        items shouldContainAll listOf(
            InvoiceItem(
                1,
                "item1",
                BigDecimal("0.500"),
                BigDecimal("10.00"),
                BigDecimal("0.00"),
                BigDecimal("5.00"),
                1
            ),
            InvoiceItem(
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

        invoiceRepository.recaculatInvoice(1)

        val invoice = invoiceRepository.getInvoice(1)
        invoice?.sum shouldBe BigDecimal("30.00")

        val items = invoiceRepository.getInvoiceDetails(1)
        items.size shouldBe 2
        items.map { it.totalPrice } shouldContainAll listOf(BigDecimal("19.00"), BigDecimal("11.00"))
    }

    @Test
    fun `should create automatic invoice with zero sum when no automatic entries exists`() {
        //given
        createAccount(accountId = 3)
        createShop(shopId = 8)
        //when
        invoiceRepository.createAutoInvoice()
        //then
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
        //given create initial state
        createAccount(accountId = 3, amount = BigDecimal("100"))
        createShop(shopId = 8)
        createCategory(2, "Cat2")
        createAssortment(1, "aso1", 2)
        createAssortment(2, "aso2", 2)
        createAutoinvoiceEntry(asoId = 1, price = BigDecimal("1.2"), quantity = BigDecimal.ONE)
        createAutoinvoiceEntry(asoId = 2, price = BigDecimal("8.9"), quantity = BigDecimal.ONE)
        //when call auto invoice procedure
        invoiceRepository.createAutoInvoice()
        //then invoice created
        val invoice = invoiceRepository.getInvoice(1)
        invoice shouldNotBe null
        invoice?.let {
            it.sum shouldBeEqualComparingTo BigDecimal("10.1")
            it.account shouldBe 3
            it.shop shouldBe 8
            it.invoiceNumber shouldStartWith "Rachunki"
        }
        //and account money amount decreased
        val account = accountRepository.findById(3)
        with(account) {
            this?.amount?.shouldBeEqualComparingTo(BigDecimal("89.9"))
        }
    }

    @Test
    fun `should create batch invoice details`() {
        //given
        createCategory(1, "Cat1")
        createCategory(2, "Cat2")
        createAssortment(1, "aso1", 2)
        createAssortment(2, "aso2", 2)
        createShopItem(1, 1)
        createShopItem(1, 2)
        createInvoice(invoiceId = 10, amount = BigDecimal.ZERO)
        //when
        invoiceRepository.createInvoiceItems(
            10,
            listOf(
                NewInvoiceItemRequest(
                    ShopItem(1, "aso1"),
                    BigDecimal("10.00"),
                    BigDecimal("0.5"),
                    BigDecimal.ZERO,
                    BigDecimal("5.00")
                ),
                NewInvoiceItemRequest(
                    ShopItem(2, "aso2"),
                    unitPrice = BigDecimal("10.00"),
                    amount = BigDecimal("1.5"),
                    discount = BigDecimal.ZERO,
                    totalPrice = BigDecimal("15.00")
                )
            )
        )
        //then
        val invoiceDetails = invoiceRepository.getInvoiceDetails(10)
        invoiceDetails.size shouldBe 2
        invoiceDetails shouldContainAll listOf(
            InvoiceItem(
                1,
                "aso1",
                BigDecimal("0.500"),
                BigDecimal("10.00"),
                BigDecimal("0.00"),
                BigDecimal("5.00"),
                1
            ),
            InvoiceItem(
                2,
                "aso2",
                BigDecimal("1.500"),
                BigDecimal("10.00"),
                BigDecimal("0.00"),
                BigDecimal("15.00"),
                2
            )
        )

    }
}