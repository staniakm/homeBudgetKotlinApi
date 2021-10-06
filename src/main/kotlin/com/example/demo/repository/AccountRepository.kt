package com.example.demo.repository

import com.example.demo.entity.Account
import com.example.demo.entity.MonthAccountSummary
import com.example.demo.entity.accountRowMapper
import com.example.demo.entity.monthAccountRowMapper
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class AccountRepository(private val helper: RepositoryHelper,
                        private val client: DatabaseClient,
                        private val queryProvider: QueryProvider) {
    fun getAccountsSummaryForMonth(date: LocalDate): Flux<MonthAccountSummary> {
        return helper.getList(queryProvider.GET_ACCOUNTS_SUMMARY_FOR_MONTH, monthAccountRowMapper) {
            bind("year", date.year)
                    .bind("month", date.monthValue)
        }
    }

    fun findAllAccounts() = helper.getList(queryProvider.GET_ACCOUNT_DATA, accountRowMapper)

    fun findById(id: Long) = helper.findFirstOrNull(queryProvider.GET_SINGLE_ACCOUNT_DATA, accountRowMapper) {
        bind("id", id)
    }

    fun update(account: Account): Mono<Void> {
        return client.sql(queryProvider.UPDATE_SINGLE_ACCOUNT_DATA)
                .bind("amount", account.amount)
                .bind("id", account.id)
                .then()
    }
}
