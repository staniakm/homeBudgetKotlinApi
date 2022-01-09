package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class AccountOperation(
    val id: Long,
    val date: LocalDate,
    val value: BigDecimal,
    val account: Int,
    val type: String
)

val operationMapper: (row: Row) -> AccountOperation = { row ->
    AccountOperation(
        row["id"] as Long,
        row["date"] as LocalDate,
        row["value"] as BigDecimal,
        row["account"] as Int,
        row["type"] as String
    )
}