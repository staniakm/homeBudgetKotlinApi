package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.math.BigDecimal
import java.time.LocalDate

data class MonthAccountSummary(val id: Int,
                               val name: String,
                               val moneyAmount: BigDecimal,
                               val expense: BigDecimal,
                               val income: BigDecimal)

val monthAccountRowMapper: (row: Row, metadata: RowMetadata) -> MonthAccountSummary = { row, _ ->
    MonthAccountSummary(row["id"] as Int,
        row["account_name"] as String,
        row["money"] as BigDecimal,
        row["expense"] as BigDecimal,
        row["income"] as BigDecimal)
}

data class Account(val id: Int, val name: String, val amount: BigDecimal, val owner: Int)

data class AccountIncomeRequest(val accountId: Int,
                                val value: BigDecimal,
                                val date: LocalDate,
                                val incomeDescription: String)

data class UpdateAccountDto(val id: Int, val name: String, val newMoneyAmount: BigDecimal)

val accountRowMapper: (row: Row, metadata:RowMetadata) -> Account = { row ,_->
    Account(row["id"] as Int, row["account_name"] as String, row["amount"] as BigDecimal, row["owner"] as Int)
}

data class AccountIncome(val id: Int,
                         val accountName: String,
                         val income: BigDecimal,
                         val date: LocalDate,
                         val description: String)

val accountIncomeRowMapper: (row: Row, metadata:RowMetadata) -> AccountIncome = { row ,_->
    AccountIncome(
        row["id"] as Int,
        row["name"] as String,
        row["income"] as BigDecimal,
        row["date"] as LocalDate,
        row["description"] as String,
    )
}
