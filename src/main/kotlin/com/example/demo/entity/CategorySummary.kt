package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class CategorySummary(
    val id: Int, val name: String, val monthSummary: BigDecimal,
    val yearSummary: BigDecimal
)

val categorySummaryRowMapper: (row: ResultSet, _: Any?) -> CategorySummary = { row, _ ->
    CategorySummary(
        row.getInt("id"),
        row.getString("name") as String,
        row.getBigDecimal("monthSummary") as BigDecimal,
        row.getBigDecimal("yearSummary") as BigDecimal,
    )
}
