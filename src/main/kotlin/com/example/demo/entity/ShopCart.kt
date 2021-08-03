package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ShopCartDetails(
    val invoiceItemId: Int, val productName: String, val quantity: BigDecimal,
    val price: BigDecimal, val discount: BigDecimal, val totalPrice: BigDecimal, val itemId: Int
)

val shopCartDetailsRowMapper: (row: Row) -> ShopCartDetails = { row ->
    ShopCartDetails(
        row.get("id", Number::class.java)!! as Int,
        row.get("nazwa", String::class.java)!!,
        row.get("ilosc", BigDecimal::class.java)!!,
        row.get("cena_za_jednostke", BigDecimal::class.java)!!,
        row.get("rabat", BigDecimal::class.java)!!,
        row.get("cena", BigDecimal::class.java)!!,
        row.get("itemId", Number::class.java)!! as Int
    )
}
