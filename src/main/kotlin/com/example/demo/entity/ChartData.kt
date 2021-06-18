package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ChartData(val name: String, val value: BigDecimal)

object ChartDataRowMapper {
    val map: (row: Row) -> ChartData = { row ->
        ChartData(
            row.get("nazwa", String::class.java)!!,
            row.get("suma", BigDecimal::class.java)!!
        )
    }

}
