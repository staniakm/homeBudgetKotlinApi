package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.Invoice
import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.NewInvoiceItemRequest
import com.example.demo.entity.NewInvoiceRequest
import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.entity.UpdateInvoiceAccountRequest
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceControllerTest : IntegrationTest() {

    @Test
    fun `should return invoices for selected month`() {
        clockProvider.setTime("2022-05-15T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "shop1")
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("10.10"), 1)
        createInvoice(2, 1, LocalDate.of(2022, 5, 11), BigDecimal("20.20"), 1)
        createInvoice(3, 1, LocalDate.of(2022, 4, 11), BigDecimal("30.30"), 1)

        val response = restTemplate.getForEntity("/api/invoice?month=0", Array<ShoppingInvoice>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 2
        response.body!!.map { it.listId } shouldContainAll listOf(1, 2)
    }

    @Test
    fun `should update invoice account`() {
        clockProvider.setTime("2022-05-15T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createAccount(2, BigDecimal("200.00"), "account2")
        createShop(1, "shop1")
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("10.10"), 1)

        val response = restTemplate.exchange(
            "/api/invoice/1",
            HttpMethod.PUT,
            HttpEntity(UpdateInvoiceAccountRequest(oldAccountId = 1, newAccount = 2, invoiceId = 1)),
            String::class.java
        )

        response.statusCode shouldBe HttpStatus.OK

        val invoices = restTemplate.getForEntity("/api/account/2?month=0", Array<ShoppingInvoice>::class.java)
        invoices.statusCode shouldBe HttpStatus.OK
        invoices.body?.size shouldBe 1
        invoices.body?.first()?.listId shouldBe 1
    }

    @Test
    fun `should return invoice details`() {
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "shop1")
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("10.10"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.10"), BigDecimal.ONE, BigDecimal("10.10"), BigDecimal.ZERO, 1, 1)

        val response = restTemplate.getForEntity("/api/invoice/1", Array<InvoiceItem>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 1
        response.body?.first()?.productName shouldBe "Milk"
    }

    @Test
    fun `should create and delete invoice`() {
        clockProvider.setTime("2022-05-15T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("200.00"), "account1")
        createShop(1, "shop1")
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)
        createShopItem(1, 1)

        val create = restTemplate.postForEntity(
            "/api/invoice",
            NewInvoiceRequest(
                accountId = 1,
                shopId = 1,
                date = LocalDate.of(2022, 5, 10),
                items = listOf(
                    NewInvoiceItemRequest(
                        shopItem = ShopItem(1, "Milk"),
                        unitPrice = BigDecimal("10.00"),
                        amount = BigDecimal.ONE,
                        discount = BigDecimal.ZERO,
                        totalPrice = BigDecimal("10.00")
                    )
                ),
                sum = BigDecimal("10.00"),
                number = "FV/1",
                description = "test"
            ),
            Invoice::class.java
        )
        create.statusCode shouldBe HttpStatus.OK
        create.body?.id shouldBe 1L

        val deleted = restTemplate.exchange("/api/invoice/1", HttpMethod.DELETE, null, Long::class.java)
        deleted.statusCode shouldBe HttpStatus.OK
        deleted.body shouldBe 1L

        val invoices = restTemplate.getForEntity("/api/account/1?month=0", Array<ShoppingInvoice>::class.java)
        invoices.statusCode shouldBe HttpStatus.OK
        invoices.body?.size shouldBe 0
    }
}
