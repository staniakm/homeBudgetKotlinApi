package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet

data class InvoiceItem(
    val invoiceItemId: Long, val productName: String, val quantity: BigDecimal,
    val price: BigDecimal, val discount: BigDecimal, val totalPrice: BigDecimal, val itemId: Int
)

val invoiceItemRowMapper: (row: ResultSet, _: Any?) -> InvoiceItem = { row ,_->
    InvoiceItem(
        row.getLong("id") ,
        row.getString("name") as String,
        row.getBigDecimal("amount") as BigDecimal,
        row.getBigDecimal("unit_price") as BigDecimal,
        row.getBigDecimal("discount") as BigDecimal,
        row.getBigDecimal("price") as BigDecimal,
        row.getInt("itemId"),
    )
}
