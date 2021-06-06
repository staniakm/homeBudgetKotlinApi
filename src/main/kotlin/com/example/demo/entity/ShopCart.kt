package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class ShopCartDetails(
    val invoiceItemId: Long, val productName: String, val quantity: BigDecimal,
    val price: BigDecimal, val discount: BigDecimal, val totalPrice: BigDecimal, val itemId: Long
)

class ShopCartDetailsRowMapper : RowMapper<ShopCartDetails> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = ShopCartDetails(
        rs.getLong("id"),
        rs.getString("nazwa"),
        rs.getBigDecimal("ilosc"),
        rs.getBigDecimal("cena_za_jednostke"),
        rs.getBigDecimal("rabat"),
        rs.getBigDecimal("cena"),
        rs.getLong("itemId")
    )
}
