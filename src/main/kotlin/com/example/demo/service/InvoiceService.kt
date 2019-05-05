package com.example.demo.service

import com.example.demo.entity.ShoppingList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService {

    @Autowired
    private lateinit var invoiceRepository: com.example.demo.repository.InvoiceRepository

    fun getInvoiceListForMonth(monthValue: Long): List<ShoppingList> {
        return invoiceRepository.getInvoices(LocalDate.now().plusMonths(monthValue))
    }

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)
}