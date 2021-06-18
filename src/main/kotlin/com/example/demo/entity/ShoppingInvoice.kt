package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class ShoppingInvoice(
    val listId: Int,
    val name: String,
    val date: LocalDate,
    val price: BigDecimal,
    val account: String
)

object ShoppingListRowMapper {
    val map: (row: Row) -> ShoppingInvoice = { row ->
        ShoppingInvoice(
            row.get("id", Number::class.java)!! as Int,
            row.get("sklep", String::class.java)!!,
            row.get("data", LocalDate::class.java)!!,
            row.get("suma", BigDecimal::class.java)!!,
            row.get("account", String::class.java)!!
        )
    }
}
