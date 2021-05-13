package com.example.demo.service

import com.example.demo.entity.ShoppingInvoice
import com.example.demo.repository.InvoiceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository) {

    fun getInvoiceListForMonth(monthValue: Long): List<ShoppingInvoice> {
        return LocalDate.now().plusMonths(monthValue)
                .let {
                    invoiceRepository.getInvoices(it)
                }
    }

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)

    fun getAccountInvoices(accountId: Long, date: LocalDate): List<ShoppingInvoice> {
        return invoiceRepository.getAccountInvoices(accountId, date)
    }
}
