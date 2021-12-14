package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ChartData(val name: String, val value: BigDecimal)

val chartDataRowMapper: (row: Row) -> ChartData = { row ->
    ChartData(
        row.get("name", String::class.java)!!,
        row.get("sum", BigDecimal::class.java)!!
    )
}
