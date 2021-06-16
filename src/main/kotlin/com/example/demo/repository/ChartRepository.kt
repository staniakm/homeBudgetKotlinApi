package com.example.demo.repository

import com.example.demo.entity.ChartData
import com.example.demo.entity.ChartDataRowMapper
import com.example.demo.repository.SqlQueries.GET_MONTH_SUMMARY_CHART_DATA
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ChartRepository(private val helper: RepositoryHelper) {

    fun getMonthSummaryChartData(date: LocalDate): List<ChartData> {
        return helper.getList(GET_MONTH_SUMMARY_CHART_DATA, ChartDataRowMapper) {
            bind(0, date)
            bind(1, date)
        }
    }
}
