package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal
import java.sql.ResultSet

data class ShopSummary(val shopId: Int, val name: String, val monthSum: BigDecimal, val yearSum: BigDecimal)

data class Shop(val shopId: Int, val name: String)

data class CreateShopRequest(val name: String)

val shopRowMapper: (row: Row, metadata: RowMetadata) -> Shop = { row, _ ->
    Shop(
        row["id"] as Int,
        row["name"] as String,
    )
}
val shopRowMapperJdbc: (row: ResultSet, _: Any?) -> Shop = { row, _ ->
    Shop(
        row.getInt("id"),
        row.getString("name") as String
    )
}

val shopSummaryRowMapperJdbc: (row: ResultSet, _: Any?) -> ShopSummary = { row, _ ->
    ShopSummary(
        row.getInt("id"),
        row.getString("name") as String,
        row.getBigDecimal("monthSum") as BigDecimal,
        row.getBigDecimal("yearSum") as BigDecimal
    )
}
