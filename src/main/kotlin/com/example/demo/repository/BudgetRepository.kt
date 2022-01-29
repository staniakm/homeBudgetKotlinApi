package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGE_DETAILS
import com.example.demo.repository.SqlQueries.GET_SINGLE_BUDGET
import com.example.demo.repository.SqlQueries.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate


@Repository
class BudgetRepository(private val helper: RepositoryHelper) {

    fun getBudgetForMonth(date: LocalDate) = getBudgetCalculations(date, getMonthBudgets(date))

    fun getSelectedBudgetItem(budgetId: Int): Mono<MonthBudgetPlanned> {
        return helper.findOne(GET_SINGLE_BUDGET, monthSingleBudgetMapper) {
            bind("$1", budgetId)
        }
    }

    fun updateBudget(updateBudget: UpdateBudgetDto): Mono<Void> {
        return helper.executeUpdate(UPDATE_MONTH_BUDGE_DETAILS) {
            bind("$1", updateBudget.planned)
                .bind("$2", updateBudget.budgetId)
        }.then(recalculateBudget(updateBudget.budgetId))
    }

    private fun recalculateBudget(budgetId: Int): Mono<Void> {
        return helper.callProcedure("call RecalculateSelectedBudget ($1)") {
            bind("$1", budgetId)
        }
    }

    private fun getBudgetCalculations(date: LocalDate, budgets: Flux<BudgetItem>): Mono<MonthBudget> {
        return getBudgetItem(date).zipWith(budgets.collectList())
            .map { t -> t.t1.copy(date = date.toString().substring(0, 7), budgets = t.t2) }
    }

    private fun getBudgetItem(date: LocalDate): Mono<MonthBudget> {
        return helper.findOne(GET_MONTH_BUDGE_DETAILS, monthBudgetMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
        }.switchIfEmpty(getRecalculatedBudget(date))
    }

    private fun getRecalculatedBudget(date: LocalDate): Mono<out MonthBudget> {
        return copyBudgetsOrCreateNew(date)
            .flatMap {
                helper.findOne(GET_MONTH_BUDGE_DETAILS, monthBudgetMapper) {
                    bind("$1", date.year)
                        .bind("$2", date.monthValue)
                }
            }.defaultIfEmpty(MonthBudget(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO))
    }

    private fun getMonthBudgets(date: LocalDate): Flux<BudgetItem> {
        return helper.getList(GET_MONTH_BUDGET, budgetItemMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
        }
    }

    fun recalculateBudgets(dateFromMonth: LocalDate): Mono<Void> {
        return helper.callProcedure("call recalculatebudget ($1)") {
            bind("$1", dateFromMonth)
        }
    }

    fun copyBudgetsOrCreateNew(date: LocalDate): Mono<Void> {
        return helper.callProcedure("call copybudgetfromlastmonthorcreatezero ($1)") {
            bind("$1", date)
        }
    }
}
