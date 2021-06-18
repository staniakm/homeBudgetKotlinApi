package com.example.demo.service

import com.example.demo.entity.ShoppingInvoice
import com.example.demo.repository.InvoiceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository, private val clock: ClockProvider) {

    fun getInvoiceListForMonth(monthValue: Long)=
        invoiceRepository.getInvoices(clock.getDateFromMonth(monthValue))

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)

    fun getAccountInvoices(accountId: Long, date: LocalDate) = invoiceRepository.getAccountInvoices(accountId, date)
}
