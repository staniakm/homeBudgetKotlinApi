package com.example.demo.repository

import com.example.demo.entity.ChartData
import com.example.demo.entity.ChartDataRowMapper
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_MONTH_SUMMARY_CHART_DATA
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.time.LocalDate

@Repository
class ChartRepository(private val jdbi: Jdbi) {

    fun getMonthSummaryChartData(date: LocalDate): List<ChartData> {

        return jdbi.withHandle<List<ChartData>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_MONTH_SUMMARY_CHART_DATA))
                    .bind(0, date)
                    .bind(1, date)
                    .map(ChartDataRowMapper())
                    .list()
        }
    }
}
