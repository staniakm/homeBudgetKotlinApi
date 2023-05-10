package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.ADD_ACCOUNT_INCOME
import com.example.demo.repository.SqlQueries.GET_ACCOUNTS_SUMMARY_FOR_MONTH
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INCOME
import com.example.demo.repository.SqlQueries.GET_INCOME_TYPES
import com.example.demo.repository.SqlQueries.GET_SINGLE_ACCOUNT_DATA
import com.example.demo.repository.SqlQueries.UPDATE_ACCOUNT_WITH_NEW_AMOUNT
import com.example.demo.repository.SqlQueries.UPDATE_SINGLE_ACCOUNT_DATA
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDate

@Repository
class AccountRepository(private val helper: RepositoryHelper) {
    fun getAccountsSummaryForMonthSkipDefaultAccount(date: LocalDate): List<MonthAccountSummary> {
        return helper
            .jdbcQueryGetList(GET_ACCOUNTS_SUMMARY_FOR_MONTH, {
                setInt(1, date.year)
                setInt(2, date.monthValue)
                setInt(3, date.year)
                setInt(4, date.monthValue)
            }, monthAccountRowMapper)
    }

    fun findAllAccounts(): List<Account> = helper
        .jdbcQueryGetList(GET_ACCOUNT_DATA, {}, accountRowMapper)

    fun findById(id: Int): Account? =
        helper.jdbcQueryGetFirst(GET_SINGLE_ACCOUNT_DATA, { setInt(1, id) }, accountRowMapper)

    fun update(account: Account): Int {
        return helper.updateJdbc(UPDATE_SINGLE_ACCOUNT_DATA) {
            setBigDecimal(1, account.amount)
            setString(2, account.name)
            setInt(3, account.id)
        }
    }

    fun getAccountIncome(accountId: Int, dateFromMonth: LocalDate): List<AccountIncome> {
        return helper.jdbcQueryGetList(GET_ACCOUNT_INCOME, {
            setInt(1, accountId)
            setInt(2, dateFromMonth.year)
            setInt(3, dateFromMonth.monthValue)
        }, accountIncomeRowMapper)
    }

    fun getIncomeTypes(): List<IncomeType> {
        return helper.jdbcQueryGetList(GET_INCOME_TYPES, {}, incomeTypeMapper)
    }

    @Transactional(transactionManager = "transactionManager")
    fun addIncome(updateAccount: AccountIncomeRequest): Int {
        return helper.updateJdbc(ADD_ACCOUNT_INCOME) {
            setInt(1, updateAccount.accountId)
            setBigDecimal(2, updateAccount.value)
            setString(3, updateAccount.incomeDescription)
            setDate(4, Date.valueOf(updateAccount.date))
        }.also {
            helper.updateJdbc(UPDATE_ACCOUNT_WITH_NEW_AMOUNT) {
                setBigDecimal(1, updateAccount.value)
                setInt(2, updateAccount.accountId)
            }
        }
    }

    @Transactional(transactionManager = "transactionManager")
    fun transferMoney(accountId: Int, value: BigDecimal, targetAccount: Int): Int {
        return helper.updateJdbc(UPDATE_ACCOUNT_WITH_NEW_AMOUNT) {
            setBigDecimal(1, value.multiply(BigDecimal.valueOf(-1)))
            setInt(2, accountId)
        }.also {
            helper.updateJdbc(UPDATE_ACCOUNT_WITH_NEW_AMOUNT) {
                setBigDecimal(1, value)
                setInt(2, targetAccount)
            }
        }
    }

    fun decreaseMoney(id: Int, sum: BigDecimal): Unit {
        helper.updateJdbc(SqlQueries.DECREASE_ACCOUNT_MONEY) {
            setBigDecimal(1, sum)
            setInt(2, id)
        }
    }

    fun increaseMoney(accountId: Int, sum: BigDecimal): Unit {
        decreaseMoney(accountId, sum.multiply(BigDecimal("-1")))
    }

    fun getOperations(accountId: Int, limit: Int): List<AccountOperation> {
        return helper.jdbcQueryGetList(SqlQueries.GET_ACCOUNT_OPERATIONS, {
            setInt(1, accountId)
            setInt(2, accountId)
            setInt(3, limit)
        }, operationMapper)
    }
}
