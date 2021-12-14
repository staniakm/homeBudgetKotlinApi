package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal
import java.time.LocalDate

data class MonthAccountSummary(
    val id: Int,
    val name: String,
    val moneyAmount: BigDecimal,
    val expense: BigDecimal,
    val income: BigDecimal
)

val monthAccountRowMapper: (row: Row) -> MonthAccountSummary = { row ->
    MonthAccountSummary(
        row["id"] as Int,
        row["account_name"] as String,
        row["money"] as BigDecimal,
        row["expense"] as BigDecimal,
        row["income"] as BigDecimal
    )
}

data class Account(
    val id: Int,
    val name: String,
    val amount: BigDecimal,
    val owner: Int
)

data class UpdateAccountDto(val id: Long, val name: String, val newMoneyAmount: BigDecimal)

val accountRowMapper: (row: Row) -> Account = { row ->
    Account(
        row["id"] as Int,
        row["account_name"] as String,
        row["amount"] as BigDecimal,
        row["owner"] as Int
    )
}

data class AccountIncome(
    val id: Int,
    val accountName: String,
    val income: BigDecimal,
    val date: LocalDate,
    val description: String
)

val accountIncomeRowMapper: (row: Row) -> AccountIncome = { row ->
    AccountIncome(
        row["id"] as Int,
        row["name"] as String,
        row["income"] as BigDecimal,
        row["date"] as LocalDate,
        row["description"] as String,
    )
}
