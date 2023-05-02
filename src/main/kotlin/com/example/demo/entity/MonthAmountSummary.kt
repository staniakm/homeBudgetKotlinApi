package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class MonthAccountSummary(
    val id: Int,
    val name: String,
    val moneyAmount: BigDecimal,
    val expense: BigDecimal,
    val income: BigDecimal
)

val monthAccountRowMapper: (row: ResultSet, _: Any?) -> MonthAccountSummary = { row, _ ->
    MonthAccountSummary(
        row.getInt("id") as Int,
        row.getString("account_name") as String,
        row.getBigDecimal("money") as BigDecimal,
        row.getBigDecimal("expense") as BigDecimal,
        row.getBigDecimal("income") as BigDecimal
    )
}

data class Account(val id: Int, val name: String, val amount: BigDecimal, val owner: Int)

data class AccountIncomeRequest(
    val accountId: Int,
    val value: BigDecimal,
    val date: LocalDate,
    val incomeDescription: String
)

data class UpdateAccountDto(val id: Int, val name: String, val newMoneyAmount: BigDecimal)

val accountRowMapper: (row: ResultSet, _: Any?) -> Account = { row, _ ->
    Account(
        row.getInt("id") as Int,
        row.getString("account_name") as String,
        row.getBigDecimal("amount") as BigDecimal,
        row.getInt("owner") as Int
    )
}

data class AccountIncome(
    val id: Int,
    val accountName: String,
    val income: BigDecimal,
    val date: LocalDate,
    val description: String
)

val accountIncomeRowMapper: (row: ResultSet, _: Any?) -> AccountIncome = { row, _ ->
    AccountIncome(
        row.getInt("id") as Int,
        row.getString("name") as String,
        row.getBigDecimal("income") as BigDecimal,
        row.getDate("date").toLocalDate() as LocalDate,
        row.getString("description") as String,
    )
}
