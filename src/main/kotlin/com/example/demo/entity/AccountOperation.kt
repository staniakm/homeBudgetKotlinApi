package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class AccountOperation(
    val id: Long,
    val date: LocalDate,
    val value: BigDecimal,
    val account: Int,
    val type: String
)

val operationMapper: (row: ResultSet, _: Any?) -> AccountOperation = { row ,_->
    AccountOperation(
        row.getLong("id") as Long,
        row.getDate("date").toLocalDate() as LocalDate,
        row.getBigDecimal("value") as BigDecimal,
        row.getInt("account") as Int,
        row.getString("type") as String
    )
}