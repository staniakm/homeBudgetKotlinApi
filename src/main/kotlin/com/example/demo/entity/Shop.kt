package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class ShopSummary(val shopId: Long, val name: String, val monthSum: BigDecimal, val yearSum: BigDecimal)

data class Shop(val shopId: Long, val name: String)

class ShopRowMapper : RowMapper<Shop> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = Shop(
        rs.getLong("id"),
        rs.getString("name")
    )
}

class ShopSummaryRowMapper : RowMapper<ShopSummary> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = ShopSummary(
        rs.getLong("id"),
        rs.getString("nazwa"),
        rs.getBigDecimal("monthSum"),
        rs.getBigDecimal("yearSum")
    )
}
