package com.example.demo.repository

import com.example.demo.entity.Account
import com.example.demo.entity.AccountRowMapper
import com.example.demo.entity.MonthAccountRowMapper
import com.example.demo.entity.MonthAccountSummary
import com.example.demo.repository.SqlQueries.GET_ACCOUNTS_SUMMARY_FOR_MONTH
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.GET_SINGLE_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.UPDATE_SINGLE_ACCOUNT_DATA
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate

@Repository
class AccountRepository(private val helper: RepositoryHelper) {
    fun getAccountsSummaryForMonth(date: LocalDate): Flux<MonthAccountSummary> {
        return helper.getList(GET_ACCOUNTS_SUMMARY_FOR_MONTH, MonthAccountRowMapper.map) {
            bind("year", date.year)
                .bind("month", date.monthValue)
        }
    }

    fun findAllAccounts() = helper.getList(GET_ACCOUNT_DATA, AccountRowMapper.map)

    fun findById(id: Long) = helper.findFirstOrNull(GET_SINGLE_ACCOUNT_DATA, AccountRowMapper.map) {
        bind(0, id)
    }

    fun update(account: Account) = helper
        .executeUpdate(UPDATE_SINGLE_ACCOUNT_DATA) {
            bind("amount", account.amount)
            bind("id", account.id)
        }
}
