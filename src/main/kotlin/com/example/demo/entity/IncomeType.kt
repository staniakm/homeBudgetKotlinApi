package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata

data class IncomeType(val id: Int, val name: String)

val incomeTypeMapper: (row: Row, metadata: RowMetadata) -> IncomeType = { row,_ ->
    IncomeType(
        row["id"] as Int, row["name"] as String
    )
}
