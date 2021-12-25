package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.ADD_ACCOUNT_INCOME
import com.example.demo.repository.SqlQueries.GET_ACCOUNTS_SUMMARY_FOR_MONTH
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INCOME
import com.example.demo.repository.SqlQueries.GET_INCOME_TYPES
import com.example.demo.repository.SqlQueries.GET_SINGLE_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.UPDATE_ACCOUNT_WITH_NEW_AMOUNT
import com.example.demo.repository.SqlQueries.UPDATE_SINGLE_ACCOUNT_DATA
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class AccountRepository(private val helper: RepositoryHelper, private val client: DatabaseClient) {
    fun getAccountsSummaryForMonth(date: LocalDate): Flux<MonthAccountSummary> {
        return helper.getList(GET_ACCOUNTS_SUMMARY_FOR_MONTH, monthAccountRowMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
        }
    }

    fun findAllAccounts() = helper.getList(GET_ACCOUNT_DATA, accountRowMapper)

    fun findById(id: Long) = helper.findFirstOrNull(GET_SINGLE_ACCOUNT_DATA, accountRowMapper) {
        bind("$1", id)
    }

    fun update(account: Account): Mono<Void> {
        return client.sql(UPDATE_SINGLE_ACCOUNT_DATA)
            .bind("$1", account.amount)
            .bind("$2", account.id)
            .then()
    }

    fun getAccountIncome(accountId: Int, dateFromMonth: LocalDate): Flux<AccountIncome> {
        return helper.getList(GET_ACCOUNT_INCOME, accountIncomeRowMapper) {
            bind("$1", dateFromMonth.year)
                .bind("$2", dateFromMonth.monthValue)
                .bind("$3", accountId)
        }
    }

    fun getIncomeTypes(): Flux<IncomeType> {
        return helper.getList(GET_INCOME_TYPES, incomeTypeMapper)
    }

    fun addIncome(updateAccount: AccountIncomeRequest): Mono<Void> {
        return client.sql(ADD_ACCOUNT_INCOME)
            .bind("$1", updateAccount.accountId)
            .bind("$2", updateAccount.value)
            .bind("$3", updateAccount.incomeDescription)
            .bind("$4", updateAccount.date)
            .then()
            .then(
                client.sql(UPDATE_ACCOUNT_WITH_NEW_AMOUNT)
                    .bind("$1", updateAccount.value)
                    .bind("$2", updateAccount.accountId)
                    .then()
            )
    }
}
