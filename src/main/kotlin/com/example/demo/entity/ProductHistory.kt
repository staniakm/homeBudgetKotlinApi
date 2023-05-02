package com.example.demo.entity

import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class ProductHistory(
    val shopName: String, val invoiceDate: LocalDate, val itemPrice: BigDecimal,
    val itemQuantity: BigDecimal, val itemDiscount: BigDecimal, val invoiceSum: BigDecimal, val invoiceId: Long,
    val invoiceDetailsId: Long, val productName: String
)

val productHistoryRowMapperJdbc: (row: ResultSet, _: Any?) -> ProductHistory = { row, _ ->
    ProductHistory(
        row.getString("shopName"),
        row.getDate("date").toLocalDate(),
        row.getBigDecimal("unitPrice"),
        row.getBigDecimal("amount"),
        row.getBigDecimal("discount"),
        row.getBigDecimal("sum"),
        row.getLong("invoiceId"),
        row.getLong("invoiceItemId"),
        row.getString("assortmentName"),
    )
}


data class Product(val id: Int, val name: String, val categoryId: Int, val categoryName: String)

val productRowMapper: (row: ResultSet, _: Any?) -> Product = { row, _ ->
    Product(
        row.getInt("id"),
        row.getString("name"),
        row.getInt("categoryId"),
        row.getString("categoryName")
    )
}

