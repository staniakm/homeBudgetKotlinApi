package com.example.demo.service

import com.example.demo.entity.*
import com.example.demo.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val invoiceService: InvoiceService,
    private val clock: ClockProvider
) {

    fun getAccountsSummaryForMonth(month: Long) =
        accountRepository.getAccountsSummaryForMonthSkipDefaultAccount(clock.getDateFromMonth(month))

    fun findAll() = accountRepository.findAllAccounts()

    fun getAccountOperations(accountId: Long, month: Long) =
        invoiceService.getAccountInvoices(accountId, clock.getDateFromMonth(month))

    fun getAccountIncome(accountId: Int, month: Long) =
        accountRepository.getAccountIncome(accountId, clock.getDateFromMonth(month))


    fun updateAccount(accountId: Int, updateAccount: UpdateAccountDto): Account? {
        if (accountId != updateAccount.id) {
            throw IllegalArgumentException("Invalid requested id")
        }

        val account: Account? = getAccount(accountId)
            ?.let {
                val newName = updateAccount.name.ifBlank { it.name }
                it.copy(amount = updateAccount.newMoneyAmount, name = newName)
            }
        return account
            ?.let {
                accountRepository.update(it)
                it
            }
    }

    fun getAccount(id: Int): Account? = accountRepository.findById(id)

    fun getIncomeTypes() = accountRepository.getIncomeTypes()
    fun addAccountIncome(updateAccount: AccountIncomeRequest): List<AccountIncome> {
        accountRepository.addIncome(updateAccount)
        return getAccountIncome(updateAccount.accountId, 0)
    }

    fun transferMoney(accountId: Int, request: TransferMoneyRequest): Account? {
        return accountRepository.findById(accountId)
            ?.let {
                accountRepository.findById(request.targetAccount)
                    ?.let {
                        accountRepository.transferMoney(request.accountId, request.value, request.targetAccount)
                        accountRepository.findById(request.targetAccount)
                    }
            }
    }

    fun getOperations(accountId: Int, limit: Int): List<AccountOperation> {
        return accountRepository.getOperations(accountId, limit)
    }
}
