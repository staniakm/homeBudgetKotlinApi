package com.example.demo.service

import com.example.demo.entity.Account
import com.example.demo.entity.UpdateAccountDto
import com.example.demo.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    fun updateAccount(accountId: Long, updateAccount: UpdateAccountDto): Account {
        if (accountId != updateAccount.id) {
            throw IllegalArgumentException("Invalid requested id")
        }

        return getAccount(accountId).copy(amount = updateAccount.newMoneyAmount)
            .let {
                accountRepository.update(it)
                it
            }
    }

    private fun getAccount(id: Long) =
        accountRepository.findById(id) ?: throw java.lang.IllegalArgumentException("Missing account")
}
