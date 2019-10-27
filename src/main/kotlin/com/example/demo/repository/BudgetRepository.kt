package com.example.demo.repository

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.MonthBudget
import com.example.demo.entity.MonthBudgetDto
import com.example.demo.repository.SqlQueries.QUERY_TYPE.UPDATE_MONTH_BUDGE_DETAILS
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.DriverManager
import java.time.LocalDate

@Repository
class BudgetRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getBudgetForMonth(date: LocalDate): BudgetItem {
        val list = ArrayList<MonthBudget>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_MONTH_BUDGET)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(MonthBudget(
                                resultSet.getString("category"),
                                resultSet.getBigDecimal("spent"),
                                resultSet.getBigDecimal("planned"),
                                resultSet.getDouble("percentage")
                        ))
                    }
                }
            }
        }
        return getBudgetCalculations(date, list)
    }

    fun getBudgetForMonthAndCategory(date: LocalDate, category: String): BudgetItem {
        val list = ArrayList<MonthBudget>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_MONTH_BUDGET_FOR_CATEGORY)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.setString(3, category)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(MonthBudget(
                                resultSet.getString("category"),
                                resultSet.getBigDecimal("spent"),
                                resultSet.getBigDecimal("planned"),
                                resultSet.getDouble("percentage")
                        ))
                    }
                }
            }
        }
        return getBudgetCalculations(date, list)
    }

    fun getBudgetCalculations(date: LocalDate, list: ArrayList<MonthBudget>): BudgetItem {
        val budget = BudgetItem(date.toString().substring(0, 7), list)
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_MONTH_BUDGE_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        budget.totalSpend = resultSet.getBigDecimal("outcome")
                        budget.totalPlanned = resultSet.getBigDecimal("planed")
                        budget.totalEarned = resultSet.getBigDecimal("income")
                    }
                }
            }
        }
        return budget
    }

    fun updateBudget(date: LocalDate, monthBudget: MonthBudgetDto) {

        val sql = SqlQueries.getQuery(UPDATE_MONTH_BUDGE_DETAILS)
        DriverManager.getConnection(connectionUrl)
                .use { con ->
                    con.prepareStatement(sql)
                            .use { statement ->
                                statement.setBigDecimal(1, monthBudget.planned)
                                statement.setString(2, monthBudget.category)
                                statement.setInt(3, date.year)
                                statement.setInt(4, date.monthValue)
                                statement.executeUpdate()
                            }
                }
    }

    @Suppress("SqlResolve", "SqlNoDataSourceInspection") //procedure call warnings
    fun recalculateBudget(date: LocalDate) {
        DriverManager.getConnection(connectionUrl)
                .use { con ->
                    con.prepareCall("{call dbo.RecalculateBudget (?)}")
                            .use { stmt ->
                                stmt.setDate(1, java.sql.Date.valueOf(date))
                                stmt.execute()
                            }
                }
    }
}