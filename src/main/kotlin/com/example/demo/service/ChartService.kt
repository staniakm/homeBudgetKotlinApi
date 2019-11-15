package com.example.demo.service

import com.example.demo.repository.ChartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChartService(private val invoiceRepository: ChartRepository) {

    fun getMonthChardData(month: Int) = invoiceRepository.getMonthSummaryChartData(month)
}