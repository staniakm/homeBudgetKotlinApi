package com.example.demo.repository

import com.example.demo.entity.ChartData
import com.example.demo.entity.chartDataRowMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate

@Repository
class ChartRepository(private val helper: RepositoryHelper,
                      private val queryProvider: QueryProvider) {

    fun getMonthSummaryChartData(date: LocalDate): Flux<ChartData> {
        return helper.getList(queryProvider.GET_MONTH_SUMMARY_CHART_DATA, chartDataRowMapper) {
            bind("date", date)
        }
    }
}
