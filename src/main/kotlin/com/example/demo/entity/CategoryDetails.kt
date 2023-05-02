package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class CategoryDetails(val name: String, val price: BigDecimal)

val categoryDetailsRowMapper: (row: ResultSet, _: Any?) -> CategoryDetails = { row,_ ->
    CategoryDetails(
        row.getString("name") as String,
        row.getBigDecimal("sum") as BigDecimal
    )
}
