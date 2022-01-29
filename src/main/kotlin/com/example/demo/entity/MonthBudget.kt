package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal


data class MonthBudget(
    val totalSpend: BigDecimal, val totalPlanned: BigDecimal,
    val totalEarned: BigDecimal, val date: String = "", val budgets: List<BudgetItem> = listOf()
)

val monthBudgetMapper: (row: Row) -> MonthBudget = { row ->
    MonthBudget(
        row["outcome"] as BigDecimal,
        row["planned"] as BigDecimal,
        row["income"] as BigDecimal,
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

val budgetItemMapper: (row: Row) -> BudgetItem = { row ->
    BudgetItem(
        row["id"] as Int,
        row["category"] as String,
        row["month"] as Int,
        row["year"] as Int,
        row["spent"] as BigDecimal,
        row["planned"] as BigDecimal,
        row["percentage"] as Int,
    )
}

val monthSingleBudgetMapper: (row: Row) -> MonthBudgetPlanned = { row ->
    MonthBudgetPlanned(
        row["id"] as Int,
        row["month"] as Int,
        row["year"] as Int,
        row["categoryId"] as Int,
        row["category"] as String,
        row["spent"] as BigDecimal,
        row["planned"] as BigDecimal,
        row["monthPlanned"] as BigDecimal,
        row["percentage"] as Int,
    )
}
