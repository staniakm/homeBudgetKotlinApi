package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.BudgetItem
import com.example.demo.entity.UpdateBudgetDto
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class BudgetRepositoryTest(@Autowired private val budgetRepository: BudgetRepository) : IntegrationTest() {

    @Test
    fun `should get single budget item`() {
        createCategory(1, "categoryName")
        createCategory(2, "categoryName2")
        createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
        createBudgetItem(2, 2, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)

        val budget = budgetRepository.getSelectedBudgetItem(1).block()!!

        budget.budgetId shouldBe 1
        budget.category shouldBe "categoryName"
        budget.planned shouldBe BigDecimal("10.00")
        budget.spent shouldBe BigDecimal("1.00")
        budget.monthPlanned shouldBe BigDecimal("20.00")
        budget.percentage shouldBe 10
    }

    @Test
    fun `should get budgets for selected month`() {
        createAccountOwner()
        createAccount()
        createIncome(1, BigDecimal("30"), LocalDate.of(2021, 11, 1))
        createCategory(1, "categoryName")
        createCategory(2, "categoryName2")
        createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
        createBudgetItem(2, 2, 11, 2021, BigDecimal("30"), BigDecimal("21.00"), 65)

        val budget = budgetRepository.getBudgetForMonth(LocalDate.of(2021, 11, 10)).block()!!

        budget.totalPlanned shouldBe BigDecimal("40.00")
        budget.totalEarned shouldBe BigDecimal("30.00")
        budget.totalSpend shouldBe BigDecimal("22.00")
        budget.date shouldBe "2021-11"
        budget.budgets.size shouldBe 2
        budget.budgets shouldContain BudgetItem(
            1,
            "categoryName",
            11,
            2021,
            BigDecimal("1.00"),
            BigDecimal("10.00"),
            10
        )
        budget.budgets shouldContain BudgetItem(
            2,
            "categoryName2",
            11,
            2021,
            BigDecimal("21.00"),
            BigDecimal("30.00"),
            65
        )
    }

    @Test
    fun `should update planed for selected budget`() {
        createCategory(1, "categoryName")
        createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal("10.00"), 10)

        budgetRepository.updateBudget(UpdateBudgetDto(1, BigDecimal("200.00"))).block()

        val budget = budgetRepository.getSelectedBudgetItem(1).block()!!

        budget.budgetId shouldBe 1
        budget.planned shouldBe BigDecimal("200.00")
        budget.spent shouldBe BigDecimal("10.00")
        budget.percentage shouldBe 5
    }


}