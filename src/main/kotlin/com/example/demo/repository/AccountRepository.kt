package com.example.demo.repository

import com.example.demo.entity.Account
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.time.LocalDate

@Repository
class AccountRepository{
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getAccountsSummaryForMonth(date: LocalDate): List<Account> {
        val accounts = ArrayList<Account>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNTS_SUMMARY_FOR_MONTH)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.setInt(3, date.year)
                statement.setInt(4, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        accounts.add(Account(
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
}