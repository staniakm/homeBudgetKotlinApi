package com.example.demo.controller

import com.example.demo.IntegrationTest
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
}
