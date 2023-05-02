package com.example.demo.service

import com.example.demo.entity.Invoice
import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.NewInvoiceRequest
import com.example.demo.entity.UpdateInvoiceAccountRequest
import com.example.demo.repository.AccountRepository
import com.example.demo.repository.InvoiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val accountRepository: AccountRepository,
    private val clock: ClockProvider
) {

    fun getInvoiceListForMonth(monthValue: Long) =
        invoiceRepository.getInvoicesForMonth(clock.getDateFromMonth(monthValue))

    fun getInvoiceDetails(id: Long) = invoiceRepository.getInvoiceDetails(id)

    fun getAccountInvoices(accountId: Long, date: LocalDate) = invoiceRepository.getAccountInvoices(accountId, date)

    @Transactional
    fun updateInvoiceAccount(invoiceId: Long, update: UpdateInvoiceAccountRequest): Invoice? {
        invoiceRepository.updateInvoiceAccount(invoiceId, update.newAccount)
        return invoiceRepository.getInvoice(invoiceId)
    }

    @Transactional(transactionManager = "transactionManager")
    fun createNewInvoiceWithItems(invoice: NewInvoiceRequest): Invoice? {
        return invoiceRepository.createInvoice(invoice)
            ?.let {
                invoiceRepository.createInvoiceItems(it.id, invoice.items)
                invoiceRepository.recaculatInvoice(it.id)
                val savedInvoice = invoiceRepository.getInvoiceJdbc(it.id)
                accountRepository.decreaseMoney(savedInvoice.account, savedInvoice.sum)
                savedInvoice
            }
    }

    fun getInvoiceItemsByCategoryAndMonth(category: Int, year: Int, month: Int): List<InvoiceItem> {
        return invoiceRepository.getInvoiceItemsByCategoryAndDate(category, year, month)
    }

    @Transactional(transactionManager = "transactionManager")
    fun deleteInvoice(invoiceId: Long): Long? {
        return invoiceRepository.getInvoice(invoiceId)
            ?.let {
                invoiceRepository.deleteDetails(it.id)
                invoiceRepository.deleteInvoice(it.id)
                accountRepository.increaseMoney(it.account, it.sum)
                invoiceId
            }
    }
}
