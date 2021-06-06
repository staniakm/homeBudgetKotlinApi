package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.QUERY_TYPE.*
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.SQLException
import java.time.LocalDate


@Repository
class BudgetRepository(private val jdbi: Jdbi) {

    fun getBudgetForMonth(date: LocalDate): BudgetItem {

        return jdbi.withHandle<List<MonthBudget>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_MONTH_BUDGET))
                .bind(0, date.year)
                .bind(1, date.monthValue)
                .map(MonthBudgetMapper()).toList()
        }.let {
            getBudgetCalculations(date, it)
        }
    }

    fun getBudgetForMonthAndCategory(date: LocalDate, category: String): BudgetItem {

        return jdbi.withHandle<List<MonthBudget>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_MONTH_BUDGET_FOR_CATEGORY))
                .bind(0, date.year)
                .bind(1, date.monthValue)
                .bind(2, category)
                .map(MonthBudgetMapper()).toList()
        }.let {
            getBudgetCalculations(date, it)
        }
    }

    fun getBudgetCalculations(date: LocalDate, list: List<MonthBudget>): BudgetItem {
        return jdbi.withHandle<BudgetItem, SQLException> { handle ->
            handle.createQuery(getQuery(GET_MONTH_BUDGE_DETAILS))
                .bind(0, date.year)
                .bind(1, date.monthValue)
                .map(BudgetItemMapper())
                .findOne()
                .map {
                    it.apply {
                        this.date = date.toString().substring(0, 7)
                        this.budgets = list
                    }
                }.orElse(BudgetItem(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO))
        }
    }

    fun updateBudget(date: LocalDate, updateBudget: UpdateBudgetDto) {

        jdbi.withHandle<Any, SQLException> { handle ->
            handle.createUpdate(getQuery(UPDATE_MONTH_BUDGE_DETAILS))
                .bind(0, updateBudget.planned)
                .bind(1, updateBudget.category)
                .bind(2, date.year)
                .bind(3, date.monthValue).execute()
        }
    }

    @Suppress("SqlResolve", "SqlNoDataSourceInspection") //procedure call warnings
    fun recalculateBudget(date: LocalDate) {
        jdbi.withHandle<Any, SQLException> { handle ->
            handle.createCall("{call dbo.RecalculateBudget (?)}")
                .bind(0, date)
                .invoke()
        }
    }
}
