package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal

data class ChartData(val name: String, val value: BigDecimal)

val chartDataRowMapper: (row: Row, metadata: RowMetadata) -> ChartData = { row,_ ->
    ChartData(
        row.get("name", String::class.java)!!,
        row.get("sum", BigDecimal::class.java)!!
    )
}
