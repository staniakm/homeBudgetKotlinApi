package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class ShoppingInvoice(
    val listId: Long,
    val name: String,
    val date: LocalDate,
    val price: BigDecimal,
    val account: String
)


val shoppingListRowMapper: (row: Row) -> ShoppingInvoice = { row ->
    ShoppingInvoice(
        row["id"] as Long,
        row["shopName"] as String,
        row["date"] as LocalDate,
        row["sum"] as BigDecimal,
        row["account_name"] as String,
    )
}
