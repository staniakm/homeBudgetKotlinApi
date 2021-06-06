package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


data class ShopItem(val itemId: Long, val name: String)

class ShopItemRowMapper : RowMapper<ShopItem> {
    override fun map(rs: ResultSet, ctx: StatementContext?) = ShopItem(
        rs.getLong("id"),
        rs.getString("nazwa")
    )

}
