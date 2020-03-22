package com.example.demo.repository

import com.example.demo.entity.Account
import com.example.demo.entity.MonthAccountSummary
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.time.LocalDate

@Repository
class AccountRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getAccountsSummaryForMonth(date: LocalDate): List<MonthAccountSummary> {
        val accounts = ArrayList<MonthAccountSummary>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNTS_SUMMARY_FOR_MONTH)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.setInt(3, date.year)
                statement.setInt(4, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        accounts.add(MonthAccountSummary(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("kwota"),
                                resultSet.getBigDecimal("wydatki"),
                                resultSet.getBigDecimal("przychody")
                        ))
                    }
                }
            }
        }
        return accounts
    }

    fun findAllAccounts(): List<Account> {
        val accounts = arrayListOf<Account>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNT_DATA)

        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).executeQuery().use { rs ->
                while (rs.next()) {
                    accounts.add(
                            Account(rs.getLong("id"),
                                    rs.getString("name"),
                                    rs.getBigDecimal("amount"),
                                    rs.getString("owner")
                            )
                    )
                }
            }
        }
        return accounts
    }
}
