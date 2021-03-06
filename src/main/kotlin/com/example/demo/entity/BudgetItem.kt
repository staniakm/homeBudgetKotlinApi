package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet


class BudgetItem(val totalSpend: BigDecimal, val totalPlanned: BigDecimal, val totalEarned: BigDecimal) {
    var date: String = ""
    var budgets: List<MonthBudget> = listOf()
}

class BudgetItemMapper : RowMapper<BudgetItem> {
    override fun map(rs: ResultSet, ctx: StatementContext): BudgetItem {
        return BudgetItem(
                rs.getBigDecimal("outcome"),
                rs.getBigDecimal("planed"),
                rs.getBigDecimal("income")
        )
    }

}


data class MonthBudget(val category: String, val spent: BigDecimal, val planned: BigDecimal, val percentage: Double)
data class MonthBudgetDto(var category: String, var planned: BigDecimal)

class MonthBudgetMapper : RowMapper<MonthBudget> {
    override fun map(rs: ResultSet, ctx: StatementContext): MonthBudget {
        return MonthBudget(
                rs.getString("category"),
                rs.getBigDecimal("spent"),
                rs.getBigDecimal("planned"),
                rs.getDouble("percentage"))
    }
}
