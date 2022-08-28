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
        setup("create 2 category and assign 1 item for each of then") {
            createCategory(1, "categoryName")
            createCategory(2, "categoryName2")
            createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
            createBudgetItem(2, 2, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
        }
        val result = methodUnderTest("fetch 1 item based on provided id") {
            budgetRepository.getSelectedBudgetItem(1).block()!!
        }
        validateResults("validate budget item data is correct") {
            with(result) {
                budgetId shouldBe 1
                category shouldBe "categoryName"
                planned shouldBe BigDecimal("10.00")
                spent shouldBe BigDecimal("1.00")
                monthPlanned shouldBe BigDecimal("20.00")
                percentage shouldBe 10
            }
        }
    }

    @Test
    fun `should get budgets for selected month`() {
        setup("prepare initial state") {
            accountOwnerCreator {
                withId = 1
                withName = "account owner 1"
                withEmptyDescription()
            }
            createAccount()
            createIncome(1, BigDecimal("30"), LocalDate.of(2021, 11, 1))
            createCategory(1, "categoryName")
            createCategory(2, "categoryName2")
            createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal.ONE, 10)
            createBudgetItem(2, 2, 11, 2021, BigDecimal("30"), BigDecimal("21.00"), 65)
        }
        val result = methodUnderTest("fetch budget for month based on provided date") {
            budgetRepository.getBudgetForMonth(LocalDate.of(2021, 11, 10)).block()!!
        }
        validateResults("result should contains 2 items", result) {
            totalPlanned shouldBe "40.00".toBigDecimal()
            totalEarned shouldBe "30.00".toBigDecimal()
            totalSpend shouldBe "22.00".toBigDecimal()
            date shouldBe "2021-11"
            budgets.size shouldBe 2
            validateResults(result = budgets.first { it.budgetId == 1 }) {
                budgetId shouldBe 1
                category shouldBe "categoryName"
                month shouldBe 11
                year shouldBe 2021
                spent shouldBe "1.00".toBigDecimal()
                planned shouldBe "10.00".toBigDecimal()
                percentage shouldBe 10
            }
            validateResults(result = budgets.first { it.budgetId == 2 }) {
                budgetId shouldBe 2
                category shouldBe "categoryName2"
                month shouldBe 11
                year shouldBe 2021
                spent shouldBe BigDecimal("21.00")
                planned shouldBe BigDecimal("30.00")
                percentage shouldBe 65
            }
        }
    }

    @Test
    fun `should update planed for selected budget`() {
        setup("prepare category with budget item") {
            createCategory(1, "categoryName")
            createBudgetItem(1, 1, 11, 2021, BigDecimal("10"), BigDecimal("10.00"), 10)
            budgetRepository.updateBudget(UpdateBudgetDto(1, BigDecimal("200.00"))).block()
        }
        val result = methodUnderTest {
            budgetRepository.getSelectedBudgetItem(1).block()!!
        }
        validateResults(result = result) {
            budgetId shouldBe 1
            planned shouldBe BigDecimal("200.00")
            spent shouldBe BigDecimal("10.00")
            percentage shouldBe 5
        }
    }
}