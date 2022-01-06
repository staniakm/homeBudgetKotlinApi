package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Account
import com.example.demo.entity.AccountIncomeRequest
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate


class AccountRepositoryTest(@Autowired private val accountRepository: AccountRepository) : IntegrationTest() {

    @Test
    fun `should get all accounts`() {
        createAccountOwner()
        createAccount()

        val findAllAccounts = accountRepository.findAllAccounts().collectList()

        findAllAccounts.block()?.size shouldBe 1
    }

    @Test
    fun `should find account by id`() {

        createAccountOwner()
        createAccount()

        val account = accountRepository.findById(1).block()!!

        account.asClue {
            it.id shouldBe 1
            it.amount shouldBe BigDecimal("1.00")
            it.name shouldBe "account"
        }
    }

    @Test
    fun `should set new money value for selected account`() {
        createAccountOwner()
        createAccount()

        accountRepository.update(Account(1, "", BigDecimal("1.23"), 1)).block()
        val account = accountRepository.findById(1).block()!!

        withClue("Account should be updated") {
            account.id shouldBe 1
            account.amount shouldBe BigDecimal("1.23")
            account.name shouldBe "account"
        }
    }

    @Test
    fun `should get account income for selected month`() {
        createAccountOwner()
        createAccount()
        createIncome(1, BigDecimal("112.10"), LocalDate.of(2021, 11, 12))

        val income = accountRepository.getAccountIncome(1, LocalDate.of(2021, 11, 30)).collectList().block()!!

        income.size shouldBe 1
        income[0].income shouldBe BigDecimal("112.10")
    }

    @Test
    fun `should get account income for selected month with multiple incomes`() {
        createAccountOwner()
        createAccount()
        createIncome(1, BigDecimal("112.10"), LocalDate.of(2021, 11, 12))
        createIncome(1, BigDecimal("100.10"), LocalDate.of(2021, 11, 1))
        createIncome(1, BigDecimal("200.10"), LocalDate.of(2021, 11, 30))

        val income = accountRepository.getAccountIncome(1, LocalDate.of(2021, 11, 30)).collectList().block()!!

        income.size shouldBe 3
        income.sumOf { it.income } shouldBe BigDecimal("412.30")
    }

    @Test
    fun `should get account income for selected month with multiple incomes from different months and accounts`() {
        createAccountOwner()
        createAccount()
        createAccount(2)
        createIncome(1, BigDecimal("112.10"), LocalDate.of(2021, 11, 12))
        createIncome(1, BigDecimal("100.10"), LocalDate.of(2021, 12, 1))
        createIncome(2, BigDecimal("200.10"), LocalDate.of(2021, 11, 30))

        val income = accountRepository.getAccountIncome(1, LocalDate.of(2021, 11, 30)).collectList().block()!!

        income.size shouldBe 1
        income[0].income shouldBe BigDecimal("112.10")
    }

    @Test
    fun `should add income with account money increase`() {
        createAccountOwner()
        createAccount(1)

        accountRepository.addIncome(
            AccountIncomeRequest(
                1,
                BigDecimal("1000.10"),
                LocalDate.of(2021, 11, 10),
                "Income test"
            )
        ).block()

        val income = accountRepository.getAccountIncome(1, LocalDate.of(2021, 11, 30)).collectList().block()!!
        val account = accountRepository.findById(1).block()!!

        account.amount shouldBe BigDecimal("1001.10")
        income.size shouldBe 1
        income[0].income shouldBe BigDecimal("1000.10")
        income[0].description shouldBe "Income test"
    }

    @Test
    fun `should transfer money`() {
        createAccountOwner()
        createAccount(1, BigDecimal(120))
        createAccount(2, BigDecimal(50))

        accountRepository.transferMoney(1, BigDecimal(50), 2).block()

        val accountSource = accountRepository.findById(1).block()!!
        val accountTarget = accountRepository.findById(2).block()!!

        accountSource.amount shouldBe BigDecimal("70.00")
        accountTarget.amount shouldBe BigDecimal("100.00")
    }

    @Test
    fun `should return account summary for month for one existing account`() {
        createAccountOwner()
        createAccount(2, amount = BigDecimal(100))
        createIncome(2, BigDecimal("100"), LocalDate.of(2021, 11, 10))
        createShop()
        createInvoice(1,2, LocalDate.of(2021, 11, 20), BigDecimal("100.10"))

        val accounts = accountRepository.getAccountsSummaryForMonthSkipDefaultAccount(LocalDate.of(2021, 11, 1)).collectList().block()!!

        accounts.size shouldBe 1
        accounts[0].id shouldBe 2
        accounts[0].income shouldBe BigDecimal("100.00")
        accounts[0].expense shouldBe BigDecimal("100.10")
        accounts[0].moneyAmount shouldBe BigDecimal("100.00")
    }

    @Test
    fun `should return account summary for month for one account with multiple income and outcome`() {
        createAccountOwner()
        createAccount(2, amount = BigDecimal(100))
        createIncome(2, BigDecimal("100"), LocalDate.of(2021, 11, 10))
        createIncome(2, BigDecimal("200"), LocalDate.of(2021, 10, 10))
        createIncome(2, BigDecimal("300"), LocalDate.of(2021, 11, 10))
        createShop()
        createInvoice(1,2, LocalDate.of(2021, 11, 20), BigDecimal("120.10"))
        createInvoice(2,2, LocalDate.of(2021, 10, 20), BigDecimal("10"))
        createInvoice(3,2, LocalDate.of(2021, 11, 1), BigDecimal("150.11"))

        val accounts = accountRepository.getAccountsSummaryForMonthSkipDefaultAccount(LocalDate.of(2021, 11, 1)).collectList().block()!!

        accounts.size shouldBe 1
        accounts[0].id shouldBe 2
        accounts[0].income shouldBe BigDecimal("400.00")
        accounts[0].expense shouldBe BigDecimal("270.21")
    }

    @Test
    fun `should return account summary for month for many accounts with multiple income and outcome`() {
        createAccountOwner()
        createAccount(2, amount = BigDecimal(200), name = "B account")
        createAccount(3, amount = BigDecimal(100), name = "A Account")
        createIncome(3, BigDecimal("100"), LocalDate.of(2021, 11, 10))
        createIncome(2, BigDecimal("200"), LocalDate.of(2021, 11, 10))
        createIncome(3, BigDecimal("300"), LocalDate.of(2021, 10, 10))
        createShop()
        createInvoice(1,3, LocalDate.of(2021, 11, 20), BigDecimal("120.10"))
        createInvoice(2,2, LocalDate.of(2021, 10, 20), BigDecimal("10"))
        createInvoice(3,2, LocalDate.of(2021, 11, 1), BigDecimal("150.11"))

        val accounts = accountRepository.getAccountsSummaryForMonthSkipDefaultAccount(LocalDate.of(2021, 11, 1)).collectList().block()!!

        withClue("There should be 2 accounts") {
            accounts.size shouldBe 2
        }
        withClue("Account with id 3 should be first on list") {
            accounts[0].id shouldBe 3
            accounts[0].income shouldBe BigDecimal("100.00")
            accounts[0].expense shouldBe BigDecimal("120.10")
        }

        withClue("Account with id 2 should be second on list") {
            accounts[1].id shouldBe 2
            accounts[1].income shouldBe BigDecimal("200.00")
            accounts[1].expense shouldBe BigDecimal("150.11")
        }
    }
}