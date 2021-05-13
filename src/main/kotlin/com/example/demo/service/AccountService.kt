package com.example.demo.service

import com.example.demo.entity.Account
import com.example.demo.entity.MonthAccountSummary
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.repository.AccountRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AccountService(private val accountRepository: AccountRepository, private val invoiceService: InvoiceService) {

    fun getAccountsSummaryForMonth(month: Long): List<MonthAccountSummary> {
        return LocalDate.now().plusMonths(month)
                .let {
                    accountRepository.getAccountsSummaryForMonth(it)
                }
    }

    fun findAll(): List<Account> {
        return accountRepository.findAllAccounts();
    }

    fun getAccountDetails(accountId: Long) {
        //account basic data

        //account details data
        //invoices for current month
    }

    fun getAccountOperations(accountId: Long, month: Long): List<ShoppingInvoice> {
        return LocalDate.now().plusMonths(month)
            .let {
                invoiceService.getAccountInvoices(accountId, it)
            }
    }

}
