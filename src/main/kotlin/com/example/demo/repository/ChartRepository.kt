package com.example.demo.repository

import com.example.demo.entity.ChartData
import com.example.demo.entity.chartDataRowMapper
import com.example.demo.repository.SqlQueries.GET_MONTH_SUMMARY_CHART_DATA
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate

@Repository
class ChartRepository(private val helper: RepositoryHelper) {

    fun getMonthSummaryChartData(date: LocalDate): Flux<ChartData> {
        return helper.getList(GET_MONTH_SUMMARY_CHART_DATA, chartDataRowMapper) {
            bind("date", date)
        }
    }
}
