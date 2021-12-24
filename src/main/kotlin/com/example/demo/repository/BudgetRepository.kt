package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGE_DETAILS
import com.example.demo.repository.SqlQueries.GET_SINGLE_BUDGET
import com.example.demo.repository.SqlQueries.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


@Repository
class BudgetRepository(private val helper: RepositoryHelper, private val client: DatabaseClient) {

    fun getBudgetForMonth(date: LocalDate) = getBudgetCalculations(date, getMonthBudgets(date))

    fun getSelectedBudgetItem(budgetId: Int) = helper.findOne(GET_SINGLE_BUDGET, monthSingleBudgetMapper) {
        bind("$1", budgetId)
    }

    private fun getBudgetCalculations(date: LocalDate, budgets: Flux<MonthBudget>): Mono<BudgetItem> {
        return getBudgetItem(date).zipWith(budgets.collectList())
            .map { t -> t.t1.copy(date = date.toString().substring(0, 7), budgets = t.t2) }
    }

    fun updateBudget(updateBudget: UpdateBudgetDto): Mono<Void> {
        return client.sql(UPDATE_MONTH_BUDGE_DETAILS)
            .bind("$1", updateBudget.planned)
            .bind("$2", updateBudget.budgetId)
            .then()
    }

    fun recalculateBudget(budgetId: Int): Mono<Void> {
        return helper.callProcedure("call RecalculateSelectedBudget ($1)") {
            bind("$1", budgetId)
        }
    }

    private fun getBudgetItem(date: LocalDate): Mono<BudgetItem> {
        return helper.findOne(GET_MONTH_BUDGE_DETAILS, budgetItemMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
        }
    }

    private fun getMonthBudgets(date: LocalDate): Flux<MonthBudget> {
        return helper.getList(GET_MONTH_BUDGET, monthBudgetMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
        }
    }
}
