package com.example.demo.service

import com.example.demo.FakeClockProvider
import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.MonthBudget
import com.example.demo.entity.MonthBudgetPlanned
import com.example.demo.repository.BudgetRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal
import java.time.LocalDate

class BudgetServiceTest {

    @Test
    fun `should recalculate budget and return month budget`() {
        val repository = mock(BudgetRepository::class.java)
        val invoiceService = mock(InvoiceService::class.java)
        val clock = FakeClockProvider("2022-05-01T00:00:00Z")
        val expectedDate = LocalDate.of(2022, 6, 1)
        val expectedBudget = MonthBudget(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, "2022-06")
        `when`(repository.getBudgetForMonth(expectedDate)).thenReturn(expectedBudget)

        val service = BudgetService(repository, invoiceService, clock)
        val result = service.recalculateBudgets(1)

        result shouldBe expectedBudget
    }

    @Test
    fun `should return empty invoice list when selected budget does not exist`() {
        val repository = mock(BudgetRepository::class.java)
        val invoiceService = mock(InvoiceService::class.java)
        val clock = FakeClockProvider()
        `when`(repository.getSelectedBudgetItem(10)).thenReturn(null)

        val service = BudgetService(repository, invoiceService, clock)
        val result = service.getBudgetItem(10)

        result shouldBe emptyList()
    }

    @Test
    fun `should return invoice items for selected budget`() {
        val repository = mock(BudgetRepository::class.java)
        val invoiceService = mock(InvoiceService::class.java)
        val clock = FakeClockProvider()
        val selected = MonthBudgetPlanned(
            budgetId = 1,
            month = 5,
            year = 2022,
            categoryId = 3,
            category = "Food",
            spent = BigDecimal.ONE,
            planned = BigDecimal.TEN,
            monthPlanned = BigDecimal.TEN,
            percentage = 10
        )
        val expected = listOf(
            InvoiceItem(1, "Milk", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, 1)
        )
        `when`(repository.getSelectedBudgetItem(1)).thenReturn(selected)
        `when`(invoiceService.getInvoiceItemsByCategoryAndMonth(3, 2022, 5)).thenReturn(expected)

        val service = BudgetService(repository, invoiceService, clock)
        val result = service.getBudgetItem(1)

        result shouldBe expected
    }
}
