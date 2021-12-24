package com.example.demo.service

import com.example.demo.entity.Account
import com.example.demo.entity.AccountIncome
import com.example.demo.entity.AccountIncomeRequest
import com.example.demo.entity.UpdateAccountDto
import com.example.demo.repository.AccountRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val invoiceService: InvoiceService,
    private val clock: ClockProvider
) {

    fun getAccountsSummaryForMonth(month: Long) =
        accountRepository.getAccountsSummaryForMonth(clock.getDateFromMonth(month))

    fun findAll() = accountRepository.findAllAccounts()

    fun getAccountOperations(accountId: Long, month: Long) =
        invoiceService.getAccountInvoices(accountId, clock.getDateFromMonth(month))

    fun getAccountIncome(accountId: Int, month: Long) =
        accountRepository.getAccountIncome(accountId, clock.getDateFromMonth(month))


    fun updateAccount(accountId: Long, updateAccount: UpdateAccountDto): Mono<Account> {
        if (accountId != updateAccount.id) {
            throw IllegalArgumentException("Invalid requested id")
        }

        val account: Mono<Account> = getAccount(accountId)
            .map {
                val newName = updateAccount.name.ifBlank { it.name }
                it.copy(amount = updateAccount.newMoneyAmount, name = newName)
            }
        return account
            .flatMap { accountRepository.update(it) }
            .then(account)
    }

    fun getAccount(id: Long) =
        accountRepository.findById(id)

    fun getIncomeTypes() = accountRepository.getIncomeTypes()
    fun addAccountIncome(updateAccount: AccountIncomeRequest): Flux<AccountIncome> {
        return accountRepository.addIncome(updateAccount)
            .thenMany(getAccountIncome(updateAccount.accountId, 0))
    }
}
