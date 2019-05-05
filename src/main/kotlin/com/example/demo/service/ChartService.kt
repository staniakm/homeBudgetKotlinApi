package com.example.demo.service

import com.example.demo.repository.ChartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChartService {

    @Autowired
    private lateinit var invoiceRepository: ChartRepository

    fun getMonthChardData(month: Int) = invoiceRepository.getMonthSummaryChartData(month)
}