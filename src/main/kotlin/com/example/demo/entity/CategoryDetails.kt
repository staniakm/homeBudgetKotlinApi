package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class CategoryDetails(val name: String, val price: BigDecimal)

val categoryDetailsRowMapper: (row: Row) -> CategoryDetails = { row ->
    CategoryDetails(
        row["name"] as String,
        row["sum"] as BigDecimal
    )
}
