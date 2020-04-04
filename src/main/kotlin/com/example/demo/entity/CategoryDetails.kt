package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

class CategoryDetails (val name: String, val price: BigDecimal)

class CategoryDetailsRowMapper: RowMapper<CategoryDetails> {
    override fun map(rs: ResultSet, ctx: StatementContext?): CategoryDetails {
        return CategoryDetails(
                rs.getString("nazwa"),
                rs.getBigDecimal("cena")
        )
    }

}
