package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGE_DETAILS
import com.example.demo.repository.SqlQueries.GET_SINGLE_BUDGET
import com.example.demo.repository.SqlQueries.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.jdbc.core.SqlParameter
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.Types
import java.time.LocalDate


@Repository
class BudgetRepository(private val helper: RepositoryHelper) {

    fun getBudgetForMonth(date: LocalDate) = getBudgetCalculations(date, getMonthBudgets(date))

    fun getSelectedBudgetItem(budgetId: Int): MonthBudgetPlanned? {
        return helper.jdbcQueryGetFirst(GET_SINGLE_BUDGET, {
            setInt(1, budgetId)
        }, monthSingleBudgetMapper)
    }

    fun updateBudget(updateBudget: UpdateBudgetDto): Int {
        return helper.updateJdbc(UPDATE_MONTH_BUDGE_DETAILS) {
            setBigDecimal(1, updateBudget.planned)
            setInt(2, updateBudget.budgetId)
        }.also {
            recalculateBudget(updateBudget.budgetId)
        }
    }

    fun recalculateBudgets(dateFromMonth: LocalDate): Unit {
        val params = listOf(
            SqlParameter("recalculation_date", Types.DATE),
        )
        return helper.callProcedureJdbc("call recalculatebudget(?)", params) {
            setDate(1, java.sql.Date.valueOf(dateFromMonth))
        }
    }

    fun copyBudgetsOrCreateNew(date: LocalDate): Unit {
        return helper.callProcedureJdbc("call copybudgetfromlastmonthorcreatezero (?)", listOf(SqlParameter("budget_date", Types.DATE))) {
            setDate(1, java.sql.Date.valueOf(date))
        }
    }

    private fun recalculateBudget(budgetId: Int): Unit {
        return helper.callProcedureJdbc("call RecalculateSelectedBudget (?)", listOf(SqlParameter("selectedbudget", Types.INTEGER))) {
            setInt(1, budgetId)
        }
    }

    private fun getBudgetCalculations(date: LocalDate, budgets: List<BudgetItem>): MonthBudget {
        return getBudgetItem(date).copy(
            date = date.toString().substring(0, 7),
            budgets = budgets
        )
    }

    private fun getBudgetItem(date: LocalDate): MonthBudget {
        return helper.jdbcQueryGetFirst(GET_MONTH_BUDGE_DETAILS, {
            setInt(1, date.year)
            setInt(2, date.monthValue)
        }, monthBudgetMapper) ?: getRecalculatedBudget(date)
    }

    private fun getRecalculatedBudget(date: LocalDate): MonthBudget {
        return copyBudgetsOrCreateNew(date)
            .let {
                helper.jdbcQueryGetFirst(GET_MONTH_BUDGE_DETAILS, {
                    setInt(1, date.year)
                    setInt(2, date.monthValue)
                }, monthBudgetMapper) ?: MonthBudget(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
            }
    }

    private fun getMonthBudgets(date: LocalDate): List<BudgetItem> {
        return helper.jdbcQueryGetList(GET_MONTH_BUDGET, {
            setInt(1, date.year)
            setInt(2, date.monthValue)
        }, budgetItemMapper)
    }


}
