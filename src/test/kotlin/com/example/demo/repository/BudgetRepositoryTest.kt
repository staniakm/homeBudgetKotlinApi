package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.UpdateBudgetDto
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class BudgetRepositoryTest(@Autowired private val budgetRepository: BudgetRepository) : IntegrationTest() {

    @Test
    fun `should get single budget item`() {
        testDataBuilder.category(1, "categoryName")
        testDataBuilder.category(2, "categoryName2")
        testDataBuilder.budgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
        testDataBuilder.budgetItem(2, 2, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)

        val result = budgetRepository.getSelectedBudgetItem(1)

        with(result!!) {
            budgetId shouldBe 1
            category shouldBe "categoryName"
            planned shouldBe BigDecimal("10.00")
            spent shouldBe BigDecimal("1.00")
            monthPlanned shouldBe BigDecimal("20.00")
            percentage shouldBe 10
        }
    }

    @Test
    fun `should get budgets for selected month`() {
        accountOwnerCreator {
            withId = 1
            withName = "account owner 1"
            withEmptyDescription()
        }
        testDataBuilder.account()
        testDataBuilder.income(1, BigDecimal("30"), LocalDate.of(2021, 11, 1))
        testDataBuilder.category(1, "categoryName")
        testDataBuilder.category(2, "categoryName2")
        testDataBuilder.budgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
        testDataBuilder.budgetItem(2, 2, 11, 2021, BigDecimal("30"), BigDecimal("21.00"), 65)

        val result = budgetRepository.getBudgetForMonth(LocalDate.of(2021, 11, 10))

        result.totalPlanned shouldBe "40.00".toBigDecimal()
        result.totalEarned shouldBe "30.00".toBigDecimal()
        result.totalSpend shouldBe "22.00".toBigDecimal()
        result.date shouldBe "2021-11"
        result.budgets.size shouldBe 2
        with(result.budgets.first { it.budgetId == 1 }) {
            budgetId shouldBe 1
            category shouldBe "categoryName"
            month shouldBe 11
            year shouldBe 2021
            spent shouldBe "1.00".toBigDecimal()
            planned shouldBe "10.00".toBigDecimal()
            percentage shouldBe 10
        }
        with(result.budgets.first { it.budgetId == 2 }) {
            budgetId shouldBe 2
            category shouldBe "categoryName2"
            month shouldBe 11
            year shouldBe 2021
            spent shouldBe BigDecimal("21.00")
            planned shouldBe BigDecimal("30.00")
            percentage shouldBe 65
        }
    }

    @Test
    fun `should update planned for selected budget`() {
        testDataBuilder.category(1, "categoryName")
        testDataBuilder.budgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal("10.00"), 10)
        budgetRepository.updateBudget(UpdateBudgetDto(1, BigDecimal("200.00")))

        val result = budgetRepository.getSelectedBudgetItem(1)

        with(result!!) {
            budgetId shouldBe 1
            planned shouldBe BigDecimal("200.00")
            spent shouldBe BigDecimal("10.00")
            percentage shouldBe 5
        }
    }

    @Test
    fun `should recalculate budgets for month without errors`() {
        budgetRepository.recalculateBudgets(LocalDate.of(2022, 5, 1))
    }

    @Test
    fun `should return zeroed budget details for month without data`() {
        val result = budgetRepository.getBudgetForMonth(LocalDate.of(2022, 5, 10))

        result.date shouldBe "2022-05"
        result.totalPlanned shouldBe BigDecimal.ZERO
        result.totalSpend shouldBe BigDecimal.ZERO
        result.totalEarned shouldBe BigDecimal.ZERO
    }
}
