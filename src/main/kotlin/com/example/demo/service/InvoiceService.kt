package com.example.demo.service

import com.example.demo.entity.Invoice
import com.example.demo.entity.InvoiceUpdateAccountRequest
import com.example.demo.entity.NewInvoiceRequest
import com.example.demo.repository.InvoiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val clock: ClockProvider
) {

    fun getInvoiceListForMonth(monthValue: Long) =
        invoiceRepository.getInvoicesForMonth(clock.getDateFromMonth(monthValue))

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)

    fun getAccountInvoices(accountId: Long, date: LocalDate) = invoiceRepository.getAccountInvoices(accountId, date)

    @Transactional
    fun updateInvoiceAccount(invoiceId: Long, update: InvoiceUpdateAccountRequest): Mono<Invoice> {
        return invoiceRepository.updateInvoiceAccount(invoiceId, update.newAccount)
            .then(invoiceRepository.getInvoice(invoiceId))
    }

    @Transactional
    fun createNewInvoiceWithItems(invoice: NewInvoiceRequest): Mono<Invoice> {
        return invoiceRepository.createInvoice(invoice)
            .flatMap {
                invoiceRepository.createInvoiceItems(it, invoice.items)
                    .then(invoiceRepository.getInvoice(it.id))
            }
    }
}
