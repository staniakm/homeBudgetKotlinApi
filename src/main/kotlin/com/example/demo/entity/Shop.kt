package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ShopSummary(val shopId: Int, val name: String, val monthSum: BigDecimal, val yearSum: BigDecimal)

data class Shop(val shopId: Int, val name: String)


val shopRowMapper: (row: Row) -> Shop = { row ->
    Shop(
        row["id"] as Int,
        row["name"] as String,
    )
}

val shopSummaryRowMapper: (row: Row) -> ShopSummary = { row ->
    ShopSummary(
        row["id"] as Int,
        row["name"] as String,
        row["monthSum"] as BigDecimal,
        row["yearSum"] as BigDecimal
    )
}
