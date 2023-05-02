package com.example.demo.repository

import com.example.demo.entity.ChartData
import com.example.demo.entity.chartDataRowMapper
import com.example.demo.repository.SqlQueries.GET_MONTH_SUMMARY_CHART_DATA
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ChartRepository(private val helper: RepositoryHelper) {

    fun getMonthSummaryChartData(date: LocalDate): List<ChartData> {
        return helper.jdbcQueryGetList(GET_MONTH_SUMMARY_CHART_DATA, {
            setInt(1, date.year)
            setInt(2, date.monthValue)
        }, chartDataRowMapper)
    }
}
