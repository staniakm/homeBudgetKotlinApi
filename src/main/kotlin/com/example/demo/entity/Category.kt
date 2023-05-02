package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class Category(
    val id: Int, val name: String, val monthSummary: BigDecimal,
    val yearSummary: BigDecimal
)

val categoryRowMapper: (row: ResultSet, _: Any?) -> Category = { row, _ ->
    Category(
        row.getInt("id") as Int,
        row.getString("name") as String,
        row.getBigDecimal("monthSummary") as BigDecimal,
        row.getBigDecimal("yearSummary") as BigDecimal,
    )
}
