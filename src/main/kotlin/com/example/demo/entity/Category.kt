package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class Category(val id: Long, val name: String, var monthSummary: BigDecimal,
               val yearSummary: BigDecimal, var details: List<CategoryDetails>)

class CategoryRowMapper : RowMapper<Category> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Category {
        return Category(
                rs.getLong("id"),
                rs.getString("nazwa"),
                rs.getBigDecimal("monthSummary"),
                rs.getBigDecimal("yearSummary"),
                emptyList())
    }
}
