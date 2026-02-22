package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.CatalogSeedItem
import com.example.demo.InvoiceItemSeed
import com.example.demo.entity.ChartData
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal

class ChartControllerTest : IntegrationTest() {

    @Test
    fun `should return empty chart data when there are no invoices`() {
        val response = restTemplate.getForEntity("/api/chart?month=0", Array<ChartData>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0
    }

    @Test
    fun `should return chart summary for selected month`() {
        val now = clockProvider.getDate()
        testDataBuilder.givenDefaultFinanceContext()
        testDataBuilder.givenCatalog(
            CatalogSeedItem(1, "Milk", 1, "Food"),
            CatalogSeedItem(2, "Soap", 2, "Home")
        )
        testDataBuilder.givenInvoiceWithTwoItems(
            invoiceId = 1,
            date = now.withDayOfMonth(10),
            firstItem = InvoiceItemSeed(1, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal("10.00"), BigDecimal.ZERO, 1, 1),
            secondItem = InvoiceItemSeed(2, BigDecimal("20.00"), BigDecimal.ONE, BigDecimal("20.00"), BigDecimal.ZERO, 2, 2)
        )
        testDataBuilder.givenInvoiceWithItems(
            invoiceId = 2,
            date = now.minusMonths(1).withDayOfMonth(10),
            items = listOf(
                InvoiceItemSeed(3, BigDecimal("9.99"), BigDecimal.ONE, BigDecimal("9.99"), BigDecimal.ZERO, 1, 1)
            )
        )

        val response = restTemplate.getForEntity("/api/chart?month=0", Array<ChartData>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 2
        response.body!!.toList() shouldContainAll listOf(
            ChartData("Food", BigDecimal("10.00")),
            ChartData("Home", BigDecimal("20.00"))
        )
    }
}
