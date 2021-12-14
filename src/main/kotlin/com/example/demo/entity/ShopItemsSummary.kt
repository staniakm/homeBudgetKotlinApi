package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ShopItemsSummary(
    val itemId: Int, val productName: String, val quantity: BigDecimal,
    val minPrice: BigDecimal, val maxPrice: BigDecimal,
    val totalDiscount: BigDecimal, val totalSpend: BigDecimal
)

val shopItemSummaryRowMapper: (row: Row) -> ShopItemsSummary = { row ->
    ShopItemsSummary(
        row["id"] as Int,
        row["name"] as String,
        row["quantity"] as BigDecimal,
        row["min_price_for_unit"] as BigDecimal,
        row["max_price_for_unit"] as BigDecimal,
        row["discount_sum"] as BigDecimal,
        row["total_spend"] as BigDecimal
    )
}
