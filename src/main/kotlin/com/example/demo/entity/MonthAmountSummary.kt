package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class MonthAccountSummary(
    val id: Int,
    val name: String,
    val moneyAmount: BigDecimal,
    val expense: BigDecimal,
    val income: BigDecimal
)

object MonthAccountRowMapper {
    val map: (row: Row) -> MonthAccountSummary = { row ->
        MonthAccountSummary(
            row.get("id", Number::class.java)!! as Int,
            row.get("nazwa", String::class.java)!!,
            row.get("kwota", BigDecimal::class.java)!!,
            row.get("wydatki", BigDecimal::class.java)!!,
            row.get("przychody", BigDecimal::class.java)!!
        )
    }
}

data class Account(
    val id: Int,
    val name: String,
    val amount: BigDecimal,
    val owner: String
)

data class UpdateAccountDto(val id: Long, val name: String, val newMoneyAmount: BigDecimal)

object AccountRowMapper {

    val map: (row: Row) -> Account = { row ->
        Account(
            row.get("id", Number::class.java)!! as Int,
            row.get("name", String::class.java)!!,
            row.get("amount", BigDecimal::class.java)!!,
            row.get("owner", String::class.java)!!
        )
    }
}
