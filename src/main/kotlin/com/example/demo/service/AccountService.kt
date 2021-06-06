package com.example.demo.service

import com.example.demo.entity.Account
import com.example.demo.entity.MonthAccountSummary
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.repository.AccountRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val invoiceService: InvoiceService,
    private val clock: ClockProvider
) {

    fun getAccountsSummaryForMonth(month: Long) =
        accountRepository.getAccountsSummaryForMonth(clock.getDateFromMonth(month))

    fun findAll() = accountRepository.findAllAccounts()

    fun getAccountDetails(accountId: Long) {
        //account basic data

        //account details data
        //invoices for current month
    }

    fun getAccountOperations(accountId: Long, month: Long) =
        invoiceService.getAccountInvoices(accountId, clock.getDateFromMonth(month))

}
