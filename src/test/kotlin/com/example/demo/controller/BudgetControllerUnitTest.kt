package com.example.demo.controller

import com.example.demo.entity.MonthBudget
import com.example.demo.service.BudgetService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal

@Tag("unit")
class BudgetControllerUnitTest {

    @Test
    fun `should return recalculated month budget`() {
        val budgetService = mock(BudgetService::class.java)
        val expected = MonthBudget(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, "2022-05")
        `when`(budgetService.recalculateBudgets(0)).thenReturn(expected)

        val controller = BudgetController(budgetService)
        val response = controller.recalculateBudget(0)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }
}
