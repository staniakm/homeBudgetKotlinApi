package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.ChartData
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class ChartControllerTest : IntegrationTest() {

    @Test
    fun `should return empty chart data when there are no invoices`() {
        val response = restTemplate.getForEntity("/api/chart?month=0", Array<ChartData>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0
    }

    @Test
    fun `should return chart summary for selected month`() {
        val now = LocalDate.now()
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "shop1")
        createCategory(1, "Food")
        createCategory(2, "Home")
        createAssortment(1, "Milk", 1)
        createAssortment(2, "Soap", 2)

        createInvoice(1, 1, now.withDayOfMonth(10), BigDecimal("30.00"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal("10.00"), BigDecimal.ZERO, 1, 1)
        createInvoiceItem(2, 1, BigDecimal("20.00"), BigDecimal.ONE, BigDecimal("20.00"), BigDecimal.ZERO, 2, 2)

        createInvoice(2, 1, now.minusMonths(1).withDayOfMonth(10), BigDecimal("9.99"), 1)
        createInvoiceItem(3, 2, BigDecimal("9.99"), BigDecimal.ONE, BigDecimal("9.99"), BigDecimal.ZERO, 1, 1)

        val response = restTemplate.getForEntity("/api/chart?month=0", Array<ChartData>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 2
        response.body!!.toList() shouldContainAll listOf(
            ChartData("Food", BigDecimal("10.00")),
            ChartData("Home", BigDecimal("20.00"))
        )
    }
}
