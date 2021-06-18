package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class ProductDetails(
    val shopName: String, val invoiceDate: LocalDate, val itemPrice: BigDecimal,
    val quantity: BigDecimal, val discount: BigDecimal, val totalSum: BigDecimal, val invoiceId: Int,
    val invoiceItemId: Int, val productName: String
)

object ProductDetailsRowMapper {
    val map: (row: Row) -> ProductDetails = { row ->
        ProductDetails(
            row.get("sklep", String::class.java)!!,
            row.get("data", LocalDate::class.java)!!,
            row.get("cena", BigDecimal::class.java)!!,
            row.get("ilosc", BigDecimal::class.java)!!,
            row.get("rabat", BigDecimal::class.java)!!,
            row.get("suma", BigDecimal::class.java)!!,
            row.get("invoiceId", Number::class.java)!! as Int,
            row.get("invoiceItemId", Number::class.java)!! as Int,
            row.get("nazwa", String::class.java)!!
        )
    }
}
