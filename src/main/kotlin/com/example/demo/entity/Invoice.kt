package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class Invoice(
    val id: Long,
    val date: LocalDate,
    val invoiceNumber: String,
    val sum: BigDecimal,
    val description: String,
    val del: Boolean,
    val account: Int,
    val shop: Int
)

val invoiceRowMapper: (row: ResultSet, _: Any?) -> Invoice = { row, _ ->
    Invoice(
        row.getLong("id"),
        row.getDate("date").toLocalDate() as LocalDate,
        row.getString("invoice_number") as String,
        row.getBigDecimal("sum") as BigDecimal,
        row.getString("description") as String,
        row.getBoolean("del"),
        row.getInt("account"),
        row.getInt("shop")
    )
}


data class NewInvoiceRequest(
    val accountId: Int,
    val shopId: Int,
    val date: LocalDate,
    val items: List<NewInvoiceItemRequest>,
    val sum: BigDecimal,
    val number: String,
    val description: String
)

data class NewInvoiceItemRequest(
    val shopItem: ShopItem, val unitPrice: BigDecimal,
    val amount: BigDecimal, val discount: BigDecimal,
    val totalPrice: BigDecimal
)

data class UpdateInvoiceAccountRequest(val oldAccountId: Int, val newAccount: Int, val invoiceId: Long)