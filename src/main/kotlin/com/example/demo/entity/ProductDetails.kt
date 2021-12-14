package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class ProductDetails(
    val shopName: String, val invoiceDate: LocalDate, val itemPrice: BigDecimal,
    val quantity: BigDecimal, val discount: BigDecimal, val totalSum: BigDecimal, val invoiceId: Long,
    val invoiceItemId: Long, val productName: String
)


val productDetailsRowMapper: (row: Row) -> ProductDetails = { row ->
    ProductDetails(
        row["shopName"] as String,
        row["date"] as LocalDate,
        row["unitPrice"] as BigDecimal,
        row["amount"] as BigDecimal,
        row["discount"] as BigDecimal,
        row["sum"] as BigDecimal,
        row["invoiceId"] as Long,
        row["invoiceItemId"] as Long,
        row["assortmentName"] as String,
    )
}
