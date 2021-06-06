package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class ChartData(val name: String, val value: BigDecimal)

class ChartDataRowMapper : RowMapper<ChartData> {
    override fun map(rs: ResultSet, ctx: StatementContext?): ChartData {
        return ChartData(
                rs.getString("nazwa"),
                rs.getBigDecimal("suma")
        )
    }

}
