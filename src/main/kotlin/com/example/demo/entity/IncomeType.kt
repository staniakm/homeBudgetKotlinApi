package com.example.demo.entity

import io.r2dbc.spi.Row

data class IncomeType(val id: Int, val name: String)

val incomeTypeMapper: (row: Row) -> IncomeType = { row ->
    IncomeType(
        row["id"] as Int, row["name"] as String
    )
}
