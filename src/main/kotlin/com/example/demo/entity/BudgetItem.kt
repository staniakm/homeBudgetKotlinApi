package com.example.demo.entity

import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.flux
import reactor.core.publisher.Flux
import java.math.BigDecimal


data class BudgetItem(
    val totalSpend: BigDecimal, val totalPlanned: BigDecimal,
    val totalEarned: BigDecimal, val date: String = "", val budgets: List<MonthBudget> = listOf()
) {
    companion object {
        fun emptyBudget(): BudgetItem {
            return BudgetItem(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        }
    }
}

class BudgetItemMapper {
    companion object {
        val map: (row: Row) -> BudgetItem = { row ->
            BudgetItem(
                row.get("outcome", BigDecimal::class.java)!!,
                row.get("planed", BigDecimal::class.java)!!,
                row.get("income", BigDecimal::class.java)!!
            )
        }
    }

}


data class MonthBudget(val category: String, val spent: BigDecimal, val planned: BigDecimal, val percentage: Int)
data class UpdateBudgetDto(var category: String, var planned: BigDecimal)

object MonthBudgetMapper {
    val map: (row: Row) -> MonthBudget = { row ->
        MonthBudget(
            row.get("category", String::class.java)!!,
            row.get("spent", BigDecimal::class.java)!!,
            row.get("planned", BigDecimal::class.java)!!,
            row.get("percentage", Number::class.java)!! as Int,
        )
    }
}
