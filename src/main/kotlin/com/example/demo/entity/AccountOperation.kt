package com.example.demo.entity

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
        row.getLong("id") ,
        row.getDate("date").toLocalDate() as LocalDate,
        row.getBigDecimal("value") as BigDecimal,
        row.getInt("account"),
        row.getString("type") as String
    )
}