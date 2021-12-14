package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ShopCartDetails(
    val invoiceItemId: Long, val productName: String, val quantity: BigDecimal,
    val price: BigDecimal, val discount: BigDecimal, val totalPrice: BigDecimal, val itemId: Int
)

val shopCartDetailsRowMapper: (row: Row) -> ShopCartDetails = { row ->
    ShopCartDetails(
        row["id"] as Long,
        row["name"] as String,
        row["amount"] as BigDecimal,
        row["unit_price"] as BigDecimal,
        row["discount"] as BigDecimal,
        row["price"] as BigDecimal,
        row["itemId"] as Int,
    )
}
