package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGET_FOR_CATEGORY
import com.example.demo.repository.SqlQueries.GET_MONTH_BUDGE_DETAILS
import com.example.demo.repository.SqlQueries.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


@Repository
class BudgetRepository(private val helper: RepositoryHelper, private val client: DatabaseClient) {

    fun getBudgetForMonth(date: LocalDate) = getBudgetCalculations(date, getMonthBudgets(date))

    fun getBudgetForMonthAndCategory(date: LocalDate, category: String) =
        getBudgetCalculations(date, getMonthBudgetForCategory(date, category))

    private fun getBudgetCalculations(date: LocalDate, budgets: Flux<MonthBudget>): Mono<BudgetItem> {
        return getBudgetItem(date).zipWith(budgets.collectList())
            .map { t -> t.t1.copy(date = date.toString().substring(0, 7), budgets = t.t2) }
    }

    fun updateBudget(date: LocalDate, updateBudget: UpdateBudgetDto) {
        client.sql(UPDATE_MONTH_BUDGE_DETAILS)
            .bind("planed", updateBudget.planned)
            .bind("category", updateBudget.category)
            .bind("year", date.year)
            .bind("month", date.monthValue)
            .then().and { recalculateBudget(date) }
            .subscribe()
//        helper.executeUpdate(UPDATE_MONTH_BUDGE_DETAILS) {
//            bind("planed", updateBudget.planned)
//                .bind("category", updateBudget.category)
//                .bind("year", date.year)
//                .bind("month", date.monthValue)
    }

    @Suppress("SqlResolve", "SqlNoDataSourceInspection") //procedure call warnings
    fun recalculateBudget(date: LocalDate) {
        client.sql("RecalculateBudget :date")
            .bind("date", date)
            .then().subscribe()
    }

    private fun getBudgetItem(date: LocalDate): Mono<BudgetItem> {
        return helper.findOne(GET_MONTH_BUDGE_DETAILS, budgetItemMapper) {
            bind("year", date.year)
                .bind("month", date.monthValue)
        }
    }

    private fun getMonthBudgetForCategory(date: LocalDate, category: String): Flux<MonthBudget> {
        return helper.getList(GET_MONTH_BUDGET_FOR_CATEGORY, monthBudgetMapper) {
            bind("year", date.year)
                .bind("month", date.monthValue)
                .bind("category", category)
        }
    }

    private fun getMonthBudgets(date: LocalDate): Flux<MonthBudget> {
        return helper.getList(GET_MONTH_BUDGET, monthBudgetMapper) {
            bind("year", date.year)
                .bind("month", date.monthValue)
        }
    }
}
