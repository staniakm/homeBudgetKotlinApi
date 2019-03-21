package com.example.demo.service

import com.example.demo.entity.ChartData
import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService {

    @Autowired
    private lateinit var repository: com.example.demo.repository.Repository

    fun getInvoiceListForMonth(monthValue: Long): List<ShoppingList> {
        return repository.getInvoices(LocalDate.now().plusMonths(monthValue))
    }

    fun getInvoiceDetails(id: Long) = repository.getInvoiceDetails(id)

    fun getMonthChardData(month: Int) = repository.getMonthSummaryChartData(month)
}