package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet

data class ProductDetails(
    val shopName: String, val invoiceDate: Date, val itemPrice: BigDecimal,
    val quantity: Double, val discount: BigDecimal, val totalSum: BigDecimal, val invoiceId: Long,
    val invoiceItemId: Long
)

object ProductDetailsRowMapper : RowMapper<ProductDetails> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = ProductDetails(
        rs.getString("sklep"),
        rs.getDate("data"),
        rs.getBigDecimal("cena"),
        rs.getDouble("ilosc"),
        rs.getBigDecimal("rabat"),
        rs.getBigDecimal("suma"),
        rs.getLong("invoiceId"),
        rs.getLong("invoiceItemId")
    )
}
