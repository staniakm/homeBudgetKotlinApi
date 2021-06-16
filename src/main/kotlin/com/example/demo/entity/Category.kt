package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class Category(val id: Long, val name: String, val monthSummary: BigDecimal,
               val yearSummary: BigDecimal, var details: List<CategoryDetails>) {
    companion object {
        val bindByDate: (date:LocalDate, Query)-> Unit = { date, query->
            query.bind(0, date.year)
            query.bind(1, date.year)
            query.bind(2, date.monthValue)
        }
    }
}

object CategoryRowMapper : RowMapper<Category> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Category {
        return Category(
                rs.getLong("id"),
                rs.getString("nazwa"),
                rs.getBigDecimal("monthSummary"),
                rs.getBigDecimal("yearSummary"),
                emptyList())
    }
}
