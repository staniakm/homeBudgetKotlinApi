package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class AccountControllerTest : IntegrationTest() {

    @Test
    fun `should return empty list when no accounts exists`() {
        val findAllAccounts = methodUnderTest("should return empty list when no accounts exists") {
            restTemplate.getForEntity("/api/account/all", Array<Account>::class.java)
        }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        findAllAccounts.body!!.size shouldBe 0
    }

    @Test
    fun `should get all accounts`() {
        setup("create multiple accounts") {
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createAccount(2, BigDecimal("11.21"), "account2")
            createAccount(3, BigDecimal("12.22"), "account3")
        }
        val findAllAccounts = methodUnderTest("should fetch existing accounts") {
            restTemplate.getForEntity("/api/account/all", Array<Account>::class.java)
        }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        with(findAllAccounts.body!!.asList()) {
            size shouldBe 3
            this.map { it.id } shouldContainAll listOf(1, 2, 3)
            this.map { it.name } shouldContainAll listOf("account1", "account2", "account3")
            this.map { it.amount } shouldContainAll listOf(
                BigDecimal("100.00"),
                BigDecimal("11.21"),
                BigDecimal("12.22")
            )
            this.map { it.owner } shouldContainAll listOf(1, 1, 1)
        }
    }

    @Test
    fun `should return empty list when no accounts summary exists for selected month`() {
        val findAllAccounts =
            methodUnderTest("should return empty list when no accounts summary exists for selected month") {
                restTemplate.getForEntity("/api/account?month=1", Array<Account>::class.java)
            }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        findAllAccounts.body!!.size shouldBe 0
    }

    @Test
    fun `should return empty list when account summary exists but not for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        setup("create multiple accounts") {
            createShop(1, "shop1")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createIncome(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10))
            createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
        }
        val findAllAccounts =
            methodUnderTest("should return empty list when account summary exists bot not for selected month") {
                restTemplate.getForEntity("/api/account?month=-1", Array<Account>::class.java)
            }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        findAllAccounts.body!!.size shouldBe 0
    }

    @Test
    fun `should return list of accounts summary for selected month`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        setup("create multiple accounts") {
            createShop(1, "shop1")
            createAccountOwner(1, "owner1")
            createAccount(2, BigDecimal("21.99"), "account2")
            createAccount(3, BigDecimal("221.99"), "account3")
            createIncome(2, BigDecimal("100.01"), LocalDate.of(2022, 5, 10))
            createInvoice(1, 2, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
            createInvoice(2, 3, LocalDate.of(2022, 5, 20), BigDecimal("100.00"), 1)
        }
        val findAllAccounts =
            methodUnderTest("should return list of account summary for selected month") {
                restTemplate.getForEntity("/api/account?month=0", Array<MonthAccountSummary>::class.java)
            }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        findAllAccounts.body!!.size shouldBe 2
        with(findAllAccounts.body!!.asList()) {
            this.map { it.id } shouldContainAll listOf(2, 3)
            this.map { it.name } shouldContainAll listOf("account2", "account3")
            with(this.first { it.id == 2 }) {
                this.income shouldBe BigDecimal("100.01")
                this.expense shouldBe BigDecimal("100.00")
                this.moneyAmount shouldBe BigDecimal("21.99")
            }
            with(this.first { it.id == 3 }) {
                this.income shouldBe BigDecimal("0")
                this.expense shouldBe BigDecimal("100.00")
                this.moneyAmount shouldBe BigDecimal("221.99")
            }
        }
    }

    @Test
    fun `should fetch accounts summary and skip default account with id 1`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        setup("create multiple accounts") {
            createShop(1, "shop1")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createAccount(2, BigDecimal("11.21"), "account2")
            createIncome(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10))
            createInvoice(1, 2, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
        }
        val findAllAccounts =
            methodUnderTest("should fetch accounts summary and skip default account with id 1") {
                restTemplate.getForEntity("/api/account?month=0", Array<MonthAccountSummary>::class.java)
            }

        findAllAccounts.statusCode shouldBe HttpStatus.OK
        findAllAccounts.body!!.size shouldBe 1
        with(findAllAccounts.body!!.asList()) {
            this.map { it.id } shouldContainAll listOf(2)
            this.map { it.name } shouldContainAll listOf("account2")
            with(this.first { it.id == 2 }) {
                this.income shouldBe BigDecimal("0")
                this.expense shouldBe BigDecimal("100.00")
                this.moneyAmount shouldBe BigDecimal("11.21")
            }
        }
    }

    @Test
    fun `should return empty list of invoices if for selected account when no invoices exists`() {
        setup("create sample data") {
            clockProvider.setTime("2022-05-20T00:00:00.00Z")
            createShop(1, "shop1")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
            createInvoice(2, 1, LocalDate.of(2022, 5, 5), BigDecimal("100.99"), 1)
        }
        val findAllInvoices =
            methodUnderTest("should return empty list of invoices if for selected account when no invoices exists") {
                restTemplate.getForEntity("/api/account/1?month=-1", Array<ShoppingInvoice>::class.java)
            }

        findAllInvoices.statusCode shouldBe HttpStatus.OK
        findAllInvoices.body!!.size shouldBe 0
    }

    @Test
    fun `should return list of account invoices for selected month`() {
        setup("create sample data") {
            clockProvider.setTime("2022-05-20T00:00:00.00Z")
            createShop(1, "shop1")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createAccount(2, BigDecimal("10.00"), "account2")
            createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
            createInvoice(2, 1, LocalDate.of(2022, 5, 5), BigDecimal("100.99"), 1)
            createInvoice(3, 1, LocalDate.of(2022, 4, 5), BigDecimal("200.99"), 1)
            createInvoice(4, 2, LocalDate.of(2022, 5, 5), BigDecimal("200.99"), 1)
        }
        val findAllInvoices =
            methodUnderTest("should return list of account invoices for selected month") {
                restTemplate.getForEntity("/api/account/1?month=0", Array<ShoppingInvoice>::class.java)
            }
        findAllInvoices.statusCode shouldBe HttpStatus.OK
        findAllInvoices.body!!.size shouldBe 2
        with(findAllInvoices.body!!.asList()) {
            this.map { it.listId } shouldContainAll listOf(1, 2)
            this.map { it.name } shouldContainAll listOf("shop1", "shop1")
            this.map { it.date } shouldContainAll listOf(LocalDate.of(2022, 5, 10), LocalDate.of(2022, 5, 5))
            this.map { it.price } shouldContainAll listOf(BigDecimal("100.00"), BigDecimal("100.99"))
            this.map { it.account } shouldContainAll listOf("account1", "account1")
        }
    }

    @Test
    fun `should return bad request response if update account request contains invalid id`() {
        setup("create sample data") {
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
        }
        val updateAccount =
            methodUnderTest("should return bad request response if update account request contains invalid id") {
                restTemplate.exchange(
                    "/api/account/2",
                    HttpMethod.PUT,
                    HttpEntity(
                        UpdateAccountDto(
                            3,
                            "new name",
                            BigDecimal("100.00"),
                        )
                    ),
                    Void::class.java
                )
            }

        updateAccount.statusCode shouldBe HttpStatus.BAD_REQUEST
    }

    @Test
    fun `should update account data`() {
        setup("create sample data") {
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("200.00"), "account1")
        }
        val updateAccount =
            methodUnderTest("should update account data") {
                restTemplate.exchange(
                    "/api/account/1",
                    HttpMethod.PUT,
                    HttpEntity(
                        UpdateAccountDto(
                            1,
                            "new name",
                            BigDecimal("100.00"),
                        )
                    ),
                    Void::class.java
                )
            }

        updateAccount.statusCode shouldBe HttpStatus.OK
        val account = restTemplate.getForEntity("/api/account/all", Array<Account>::class.java).body!!.toList().first()
        account.name shouldBe "new name"
        account.amount shouldBe BigDecimal("100.00")
    }

    @Test
    fun `should fetch empty list when no income exists for selected account and month`() {
        setup("create sample data") {
            clockProvider.setTime("2022-05-20T00:00:00.00Z")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createIncome(1, BigDecimal("100.00"), LocalDate.of(2022, 4, 10))
            createIncome(1, BigDecimal("100.99"), LocalDate.of(2022, 3, 5))
        }
        val findAllIncomes =
            methodUnderTest("should fetch empty list when no income exists for selected account and month") {
                restTemplate.getForEntity("/api/account/1/income?month=0", Array<AccountIncome>::class.java)
            }

        findAllIncomes.statusCode shouldBe HttpStatus.OK
        findAllIncomes.body!!.size shouldBe 0
    }

    @Test
    fun `should fetch account incomes for selected account and month`() {
        setup("create sample data") {
            clockProvider.setTime("2022-05-20T00:00:00.00Z")
            createAccountOwner(1, "owner1")
            createAccount(1, BigDecimal("100.00"), "account1")
            createAccount(2, BigDecimal("100.00"), "account1")
            createIncome(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10))
            createIncome(1, BigDecimal("100.99"), LocalDate.of(2022, 5, 5))
            createIncome(1, BigDecimal("100.99"), LocalDate.of(2022, 4, 5))
            createIncome(2, BigDecimal("200.99"), LocalDate.of(2022, 5, 5))
        }
        val findAllIncomes =
            methodUnderTest("should fetch account incomes for selected account and month") {
                restTemplate.getForEntity("/api/account/1/income?month=0", Array<AccountIncome>::class.java)
            }

        findAllIncomes.statusCode shouldBe HttpStatus.OK
        findAllIncomes.body!!.size shouldBe 2
        with(findAllIncomes.body!!.asList()) {
            this.map { it.id } shouldContainAll listOf(1)
            this.map { it.income } shouldContainAll listOf(BigDecimal("100.00"), BigDecimal("100.99"))
            this.map { it.date } shouldContainAll listOf(LocalDate.of(2022, 5, 10), LocalDate.of(2022, 5, 5))
        }
    }
}