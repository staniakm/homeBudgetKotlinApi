package com.example.demo.repository

import com.example.demo.entity.Account
import com.example.demo.entity.AccountRowMapper
import com.example.demo.entity.MonthAccountRowMapper
import com.example.demo.entity.MonthAccountSummary
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.time.LocalDate

@Repository
class AccountRepository(private val jdbi: Jdbi) {
    fun getAccountsSummaryForMonth(date: LocalDate): List<MonthAccountSummary> {
        return jdbi.withHandle<List<MonthAccountSummary>, SQLException> { handle ->
            handle.createQuery(SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNTS_SUMMARY_FOR_MONTH))
                    .bind(0, date.year)
                    .bind(1, date.monthValue)
                    .bind(2, date.year)
                    .bind(3, date.monthValue)
                    .map(MonthAccountRowMapper())
                    .list()
        }
    }

    fun findAllAccounts(): List<Account> {
        return jdbi.withHandle<List<Account>, SQLException> { handle ->
            handle.createQuery(SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNT_DATA))
                    .map(AccountRowMapper())
                    .list()
        }
    }
}
