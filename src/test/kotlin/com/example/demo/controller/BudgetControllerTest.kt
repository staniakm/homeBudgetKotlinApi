package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.MonthBudget
import com.example.demo.entity.UpdateBudgetDto
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class BudgetControllerTest : IntegrationTest() {

    @Test
    fun `should return month budget for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createIncome(1, BigDecimal("500.00"), LocalDate.of(2022, 5, 2))
        createCategory(1, "Food")
        createCategory(2, "Bills")
        createBudgetItem(1, 1, 5, 2022, BigDecimal("100.00"), BigDecimal("20.00"), 20)
        createBudgetItem(2, 2, 5, 2022, BigDecimal("50.00"), BigDecimal("10.00"), 20)

        val response = restTemplate.getForEntity("/api/budget?month=0", MonthBudget::class.java)

        response.statusCode shouldBe HttpStatus.OK
        with(response.body!!) {
            totalPlanned shouldBe BigDecimal("150.00")
            totalSpend shouldBe BigDecimal("30.00")
            totalEarned shouldBe BigDecimal("500.00")
            date shouldBe "2022-05"
            budgets.size shouldBe 2
            budgets.map { it.budgetId } shouldContainAll listOf(1, 2)
        }
    }

    @Test
    fun `should return empty list when budget item does not exist`() {
        val response = restTemplate.getForEntity("/api/budget/999", Array<InvoiceItem>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0
    }

    @Test
    fun `should return invoice items for selected budget item`() {
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "shop1")
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)
        createBudgetItem(1, 1, 5, 2022, BigDecimal("100.00"), BigDecimal("20.00"), 20)
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("10.00"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal("10.00"), BigDecimal.ZERO, 1, 1)

        val response = restTemplate.getForEntity("/api/budget/1", Array<InvoiceItem>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 1
        response.body?.first()?.productName shouldBe "Milk"
    }

    @Test
    fun `should update budget planned value`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createCategory(1, "Food")
        createBudgetItem(1, 1, 5, 2022, BigDecimal("100.00"), BigDecimal("10.00"), 10)

        val update = restTemplate.exchange(
            "/api/budget",
            HttpMethod.PUT,
            HttpEntity(UpdateBudgetDto(1, BigDecimal("250.00"))),
            String::class.java
        )

        update.statusCode shouldBe HttpStatus.OK

        val monthBudget = restTemplate.getForEntity("/api/budget?month=0", MonthBudget::class.java)
        monthBudget.statusCode shouldBe HttpStatus.OK
        monthBudget.body?.budgets?.first { it.budgetId == 1 }?.planned shouldBe BigDecimal("250.00")
    }
}
