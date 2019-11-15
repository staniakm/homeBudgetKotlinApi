package com.example.demo.service

import com.example.demo.entity.ShoppingList
import com.example.demo.repository.InvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository) {

    fun getInvoiceListForMonth(monthValue: Long): List<ShoppingList> {
        return LocalDate.now().plusMonths(monthValue)
                .let {
                    invoiceRepository.getInvoices(it)
                }
    }

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)
}