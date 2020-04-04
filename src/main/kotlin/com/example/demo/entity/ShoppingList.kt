package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet

data class ShoppingList(val listId: Long, val name: String, val date: Date, val price: BigDecimal, val account: String)

class ShoppingListRowMapper : RowMapper<ShoppingList> {
    override fun map(rs: ResultSet, ctx: StatementContext?): ShoppingList {
        return ShoppingList(
                rs.getLong("id"),
                rs.getString("sklep"),
                rs.getDate("data"),
                rs.getBigDecimal("suma"),
                rs.getString("account")
        )
    }

}
