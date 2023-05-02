package com.example.demo.entity

import java.sql.ResultSet

data class IncomeType(val id: Int, val name: String)

val incomeTypeMapper: (row: ResultSet, _: Any?) -> IncomeType = { row,_ ->
    IncomeType(
        row.getInt("id") as Int, row.getString("name") as String
    )
}
