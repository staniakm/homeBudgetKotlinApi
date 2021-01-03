package com.example.demo.service

import com.example.demo.repository.ChartRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ChartService(private val invoiceRepository: ChartRepository) {

    fun getMonthChardData(date: LocalDate) = invoiceRepository.getMonthSummaryChartData(date)
}
