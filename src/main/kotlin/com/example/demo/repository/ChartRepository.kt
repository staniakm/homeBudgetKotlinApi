package com.example.demo.repository

import com.example.demo.entity.ChartData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager

@Repository
class ChartRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getMonthSummaryChartData(month: Int): List<ChartData> {
        val list = ArrayList<ChartData>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_MONTH_SUMMARY_CHART_DATA)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, month)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ChartData(
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("suma")
                        ))
                    }
                }
            }
        }
        return list
    }
}