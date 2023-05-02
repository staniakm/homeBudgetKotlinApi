package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class ChartData(val name: String, val value: BigDecimal)

val chartDataRowMapper: (row: ResultSet, _: Any?) -> ChartData = { row, _ ->
    ChartData(
        row.getString("name"),
        row.getBigDecimal("sum")
    )
}
