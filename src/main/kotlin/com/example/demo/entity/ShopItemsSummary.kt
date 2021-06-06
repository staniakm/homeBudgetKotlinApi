package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class ShopItemsSummary(
    val itemId: Long, val productName: String, val quantity: BigDecimal,
    val minPrice: BigDecimal, val maxPrice: BigDecimal,
    val totalDiscount: BigDecimal, val totalSpend: BigDecimal
)

class ShopItemSummaryRowMapper : RowMapper<ShopItemsSummary> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = ShopItemsSummary(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getBigDecimal("quantity"),
        rs.getBigDecimal("min_price_for_unit"),
        rs.getBigDecimal("max_price_for_unit"),
        rs.getBigDecimal("discount_sum"),
        rs.getBigDecimal("total_spend")
    )

}
