package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET_FOR_CATEGORY
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGE_DETAILS
import com.example.demo.repository.SqlQueries.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*


@Repository
class BudgetRepository(private val helper: RepositoryHelper) {

    fun getBudgetForMonth(date: LocalDate) = getMonthBudgets(date)
        .let {
            getBudgetCalculations(date, it)
        }

    fun getBudgetForMonthAndCategory(date: LocalDate, category: String) =
        getBudgetCalculations(date, getMonthBudgetForCategory(date, category))

    fun getBudgetCalculations(date: LocalDate, budgets: List<MonthBudget>): BudgetItem {
        return getBudgetItem(date)
            .map {
                it.copy(date = date.toString().substring(0, 7), budgets = budgets)
            }.orElse(BudgetItem.emptyBudget())
    }

    fun updateBudget(date: LocalDate, updateBudget: UpdateBudgetDto) {
        helper.executeUpdate(UPDATE_MONTH_BUDGE_DETAILS) {
            bind(0, updateBudget.planned)
            bind(1, updateBudget.category)
            bind(2, date.year)
            bind(3, date.monthValue)
        }
    }

    @Suppress("SqlResolve", "SqlNoDataSourceInspection") //procedure call warnings
    fun recalculateBudget(date: LocalDate) {
        helper.callProcedure("{call dbo.RecalculateBudget (?)}") {
            bind(0, date)
        }
    }

    private fun getBudgetItem(date: LocalDate): Optional<BudgetItem> {
        return helper.findOne(GET_MONTH_BUDGE_DETAILS, BudgetItemMapper()) {
            bind(0, date.year)
            bind(1, date.monthValue)
        }
    }

    private fun getMonthBudgetForCategory(date: LocalDate, category: String): List<MonthBudget> {
        return helper.getList(GET_MONTH_BUDGET_FOR_CATEGORY, MonthBudgetMapper()) {
            bind(0, date.year)
            bind(1, date.monthValue)
            bind(2, category)
        }
    }

    private fun getMonthBudgets(date: LocalDate): List<MonthBudget> {
        return helper.getList(GET_MONTH_BUDGET, MonthBudgetMapper()) {
            bind(0, date.year)
            bind(1, date.monthValue)
        }
    }
}
