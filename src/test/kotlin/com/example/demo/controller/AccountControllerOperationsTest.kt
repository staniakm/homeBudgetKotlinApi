package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class AccountControllerOperationsTest : IntegrationTest() {

    @Test
    fun `should fetch empty list when no income exists for selected account and month`() {
        testDataBuilder.givenOwnerAndPrimaryAccount()
        testDataBuilder.income(1, BigDecimal("100.00"), LocalDate.of(2022, 4, 10))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 3, 5))

        val findAllIncomes = restTemplate.getForEntity("/api/account/1/income?month=0", Array<AccountIncome>::class.java)

        findAllIncomes.statusCode shouldBe HttpStatus.OK
        findAllIncomes.body!!.size shouldBe 0
    }

    @Test
    fun `should fetch account incomes for selected account and month`() {
        testDataBuilder.givenOwnerAndPrimaryAccount()
        testDataBuilder.account(2, BigDecimal("100.00"), "account1")
        testDataBuilder.income(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 5, 5))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 4, 5))
        testDataBuilder.income(2, BigDecimal("200.99"), LocalDate.of(2022, 5, 5))

        val findAllIncomes = restTemplate.getForEntity("/api/account/1/income?month=0", Array<AccountIncome>::class.java)

        findAllIncomes.statusCode shouldBe HttpStatus.OK
        findAllIncomes.body!!.size shouldBe 2
        with(findAllIncomes.body!!.asList()) {
            this.map { it.id } shouldContainAll listOf(1)
            this.map { it.income } shouldContainAll listOf(BigDecimal("100.00"), BigDecimal("100.99"))
            this.map { it.date } shouldContainAll listOf(LocalDate.of(2022, 5, 10), LocalDate.of(2022, 5, 5))
        }
    }

    @Test
    fun `should add new account income`() {
        testDataBuilder.givenOwnerAndPrimaryAccount()

        val addIncome = restTemplate.postForEntity(
            "/api/account/1",
            AccountIncomeRequest(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10), "income1"),
            Void::class.java
        )

        addIncome.statusCode shouldBe HttpStatus.OK
        val account = restTemplate.getForEntity("/api/account/all", Array<Account>::class.java).body!!.toList().first()
        account.amount shouldBe BigDecimal("200.00")
        val incomes = restTemplate.getForEntity("/api/account/1/income?month=0", Array<AccountIncome>::class.java).body!!.toList()
        incomes.size shouldBe 1
        with(incomes.first()) {
            id shouldBe 1
            income shouldBe BigDecimal("100.00")
            date shouldBe LocalDate.of(2022, 5, 10)
            description shouldBe "income1"
        }
    }

    @Test
    fun `should fetch list of income types`() {
        testDataBuilder.incomeType(1, "type1")
        testDataBuilder.incomeType(2, "type2")

        val findAllIncomeTypes = restTemplate.getForEntity("/api/account/income/type", Array<IncomeType>::class.java)

        findAllIncomeTypes.statusCode shouldBe HttpStatus.OK
        findAllIncomeTypes.body!!.size shouldBe 2
        findAllIncomeTypes.body!!.asList() shouldContainAll listOf(IncomeType(1, "type1"), IncomeType(2, "type2"))
    }

    @Test
    fun `should return empty list if no income type exist`() {
        val findAllIncomeTypes = restTemplate.getForEntity("/api/account/income/type", Array<IncomeType>::class.java)

        findAllIncomeTypes.statusCode shouldBe HttpStatus.OK
        findAllIncomeTypes.body!!.size shouldBe 0
    }

    @Test
    fun `should transfer money from one account to another`() {
        testDataBuilder.givenOwnerWithTwoAccounts()

        restTemplate.put("/api/account/1/transfer", TransferMoneyRequest(1, BigDecimal("50.00"), 2))

        val accounts = restTemplate.getForEntity("/api/account/all", Array<Account>::class.java).body!!.toList()
        accounts.first { it.id == 1 }.amount shouldBe BigDecimal("100.00")
        accounts.first { it.id == 2 }.amount shouldBe BigDecimal("160.00")
    }

    @Test
    fun `should post request for transfer money between accounts and return updated source account`() {
        testDataBuilder.givenOwnerWithTwoAccounts()

        val transferMoney = restTemplate.postForEntity(
            "/api/account/transfer",
            TransferMoneyRequest(1, BigDecimal("50.00"), 2),
            Account::class.java
        )

        transferMoney.statusCode shouldBe HttpStatus.OK
        with(transferMoney.body!!) {
            id shouldBe 1
            name shouldBe "account1"
            amount shouldBe BigDecimal("100.00")
        }
    }

    @Test
    fun `should get empty list when try to fetch operations when no data exists`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.account(1, BigDecimal("150.00"), "account1")

        val findAllOperations = restTemplate.getForEntity("/api/account/1/operations", Array<AccountOperation>::class.java)

        findAllOperations.statusCode shouldBe HttpStatus.OK
        findAllOperations.body!!.size shouldBe 0
    }

    @Test
    fun `should fetch income operations when no other operations exist`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.account(1, BigDecimal("150.00"), "account1")
        testDataBuilder.income(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 10))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 5, 5))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 4, 5))

        val findAllOperations = restTemplate.getForEntity("/api/account/1/operations", Array<AccountOperation>::class.java)

        findAllOperations.statusCode shouldBe HttpStatus.OK
        findAllOperations.body!!.size shouldBe 3
        with(findAllOperations.body!!.asList()) {
            map { it.id } shouldContainAll listOf(1, 2, 3)
            map { it.value } shouldContainAll listOf(BigDecimal("100.00"), BigDecimal("100.99"), BigDecimal("100.99"))
            map { it.date } shouldContainAll listOf(LocalDate.of(2022, 5, 10), LocalDate.of(2022, 5, 5), LocalDate.of(2022, 4, 5))
            map { it.type } shouldContainAll listOf("INCOME", "INCOME", "INCOME")
        }
    }

    @Test
    fun `should fetch only outcome operations when no other operations exist`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.account(1, BigDecimal("150.00"), "account1")
        testDataBuilder.shop(1, "shop1")
        testDataBuilder.invoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
        testDataBuilder.invoice(2, 1, LocalDate.of(2022, 5, 5), BigDecimal("100.99"), 1)
        testDataBuilder.invoice(3, 1, LocalDate.of(2022, 4, 5), BigDecimal("100.99"), 1)

        val findAllOperations = restTemplate.getForEntity("/api/account/1/operations", Array<AccountOperation>::class.java)

        findAllOperations.statusCode shouldBe HttpStatus.OK
        findAllOperations.body!!.size shouldBe 3
        with(findAllOperations.body!!.asList()) {
            map { it.id } shouldContainAll listOf(1, 2, 3)
            map { it.value } shouldContainAll listOf(BigDecimal("100.00"), BigDecimal("100.99"), BigDecimal("100.99"))
            map { it.date } shouldContainAll listOf(LocalDate.of(2022, 5, 10), LocalDate.of(2022, 5, 5), LocalDate.of(2022, 4, 5))
            map { it.type } shouldContainAll listOf("OUTCOME", "OUTCOME", "OUTCOME")
        }
    }

    @Test
    fun `should find mixed list of operations sorted by date and limited to 5`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.account(1, BigDecimal("150.00"), "account1")
        testDataBuilder.shop(1, "shop1")
        testDataBuilder.invoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("100.00"), 1)
        testDataBuilder.invoice(2, 1, LocalDate.of(2022, 5, 5), BigDecimal("100.99"), 1)
        testDataBuilder.invoice(3, 1, LocalDate.of(2022, 4, 5), BigDecimal("100.99"), 1)
        testDataBuilder.income(1, BigDecimal("100.00"), LocalDate.of(2022, 5, 11))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 5, 6))
        testDataBuilder.income(1, BigDecimal("100.99"), LocalDate.of(2022, 4, 6))

        val findAllOperations = restTemplate.getForEntity("/api/account/1/operations?limit=5", Array<AccountOperation>::class.java)

        findAllOperations.statusCode shouldBe HttpStatus.OK
        findAllOperations.body!!.size shouldBe 5
        val operations = findAllOperations.body!!.asList()
        with(operations[0]) {
            id shouldBe 1
            value shouldBe BigDecimal("100.00")
            date shouldBe LocalDate.of(2022, 5, 11)
            type shouldBe "INCOME"
        }
        with(operations[1]) {
            id shouldBe 1
            value shouldBe BigDecimal("100.00")
            date shouldBe LocalDate.of(2022, 5, 10)
            type shouldBe "OUTCOME"
        }
        with(operations[2]) {
            id shouldBe 2
            value shouldBe BigDecimal("100.99")
            date shouldBe LocalDate.of(2022, 5, 6)
            type shouldBe "INCOME"
        }
        with(operations[3]) {
            id shouldBe 2
            value shouldBe BigDecimal("100.99")
            date shouldBe LocalDate.of(2022, 5, 5)
            type shouldBe "OUTCOME"
        }
        with(operations[4]) {
            id shouldBe 3
            value shouldBe BigDecimal("100.99")
            date shouldBe LocalDate.of(2022, 4, 6)
            type shouldBe "INCOME"
        }
    }

}
