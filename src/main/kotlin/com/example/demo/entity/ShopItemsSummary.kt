package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class ShopItemsSummary(
    val itemId: Int, val productName: String, val quantity: BigDecimal,
    val minPrice: BigDecimal, val maxPrice: BigDecimal,
    val totalDiscount: BigDecimal, val totalSpend: BigDecimal
)

val shopItemSummaryRowMapperJdbc: (row: ResultSet, _: Any?) -> ShopItemsSummary = { row, _ ->
    ShopItemsSummary(
        row.getInt("id") as Int,
        row.getString("name") as String,
        row.getBigDecimal("quantity") as BigDecimal,
        row.getBigDecimal("min_price_for_unit") as BigDecimal,
        row.getBigDecimal("max_price_for_unit") as BigDecimal,
        row.getBigDecimal("discount_sum") as BigDecimal,
        row.getBigDecimal("total_spend") as BigDecimal
    )
}
