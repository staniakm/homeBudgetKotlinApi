package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet


data class MonthBudget(
    val totalSpend: BigDecimal, val totalPlanned: BigDecimal,
    val totalEarned: BigDecimal, val date: String = "", val budgets: List<BudgetItem> = listOf()
)

val monthBudgetMapper: (row: ResultSet, _: Any?) -> MonthBudget = { row, _ ->
    MonthBudget(
        row.getBigDecimal("outcome") as BigDecimal,
        row.getBigDecimal("planned") as BigDecimal,
        row.getBigDecimal("income") as BigDecimal,
    )
}

data class BudgetItem(
    val budgetId: Int,
    val category: String,
    val month: Int,
    val year: Int,
    val spent: BigDecimal,
    val planned: BigDecimal,
    val percentage: Int
)

data class MonthBudgetPlanned(
    val budgetId: Int,
    val month: Int,
    val year: Int,
    val categoryId: Int,
    val category: String,
    val spent: BigDecimal,
    val planned: BigDecimal,
    val monthPlanned: BigDecimal,
    val percentage: Int
)

data class UpdateBudgetDto(val budgetId: Int, var planned: BigDecimal)

val budgetItemMapper: (row: ResultSet, _: Any?) -> BudgetItem = { row, _ ->
    BudgetItem(
        row.getInt("id"),
        row.getString("category") as String,
        row.getInt("month"),
        row.getInt("year"),
        row.getBigDecimal("spent") as BigDecimal,
        row.getBigDecimal("planned") as BigDecimal,
        row.getInt("percentage"),
    )
}

val monthSingleBudgetMapper: (row: ResultSet, _: Any?) -> MonthBudgetPlanned = { row, _ ->
    MonthBudgetPlanned(
        row.getInt("id"),
        row.getInt("month"),
        row.getInt("year"),
        row.getInt("categoryId"),
        row.getString("category") as String,
        row.getBigDecimal("spent") as BigDecimal,
        row.getBigDecimal("planned") as BigDecimal,
        row.getBigDecimal("monthPlanned") as BigDecimal,
        row.getInt("percentage"),
    )
}
