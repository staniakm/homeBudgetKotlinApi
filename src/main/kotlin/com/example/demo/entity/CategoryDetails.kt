package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal

data class CategoryDetails(val name: String, val price: BigDecimal)

val categoryDetailsRowMapper: (row: Row, metadata: RowMetadata) -> CategoryDetails = { row,_ ->
    CategoryDetails(
        row["name"] as String,
        row["sum"] as BigDecimal
    )
}
