package com.example.demo.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.math.BigDecimal
import java.sql.ResultSet

data class MonthAccountSummary(
    val id: Long,
    val name: String,
    val moneyAmount: BigDecimal,
    val expense: BigDecimal,
    val income: BigDecimal
)


class MonthAccountRowMapper : RowMapper<MonthAccountSummary> {

    override fun map(rs: ResultSet, ctx: StatementContext) = MonthAccountSummary(
        rs.getLong("id"),
        rs.getString("nazwa"),
        rs.getBigDecimal("kwota"),
        rs.getBigDecimal("wydatki"),
        rs.getBigDecimal("przychody")
    )
}

data class Account(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val owner: String
)

class AccountRowMapper : RowMapper<Account> {

    override fun map(rs: ResultSet, ctx: StatementContext) = Account(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getBigDecimal("amount"),
        rs.getString("owner")
    )
}
