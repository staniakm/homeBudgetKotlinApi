package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class ShoppingInvoice(
    val listId: Long,
    val name: String,
    val date: LocalDate,
    val price: BigDecimal,
    val account: String
)


val shoppingListRowMapper: (row: ResultSet, _: Any?) -> ShoppingInvoice = { row,_ ->
    ShoppingInvoice(
        row.getLong("id"),
        row.getString("shopName") as String,
        row.getDate("date").toLocalDate() as LocalDate,
        row.getBigDecimal("sum") as BigDecimal,
        row.getString("account_name") as String,
    )
}
