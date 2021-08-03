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
        row.get("id", Number::class.java)!! as Int,
        row.get("name", String::class.java)!!,
        row.get("quantity", BigDecimal::class.java)!!,
        row.get("min_price_for_unit", BigDecimal::class.java)!!,
        row.get("max_price_for_unit", BigDecimal::class.java)!!,
        row.get("discount_sum", BigDecimal::class.java)!!,
        row.get("total_spend", BigDecimal::class.java)!!
    )
}
