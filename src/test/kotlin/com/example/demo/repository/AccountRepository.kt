package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Account
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal


class AccountRepositoryTest : IntegrationTest() {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `should get all accounts`() {
        createAccountOwner()
        createAccount()

        val findAllAccounts = accountRepository.findAllAccounts().collectList()

        findAllAccounts.block()?.size shouldBe 1
    }

    @Test
    internal fun `should find account by id`() {

        createAccountOwner()
        createAccount()

        val account = accountRepository.findById(1L).block()!!

        account.asClue {
            it.id shouldBe 1
            it.amount shouldBe BigDecimal("1.00")
            it.name shouldBe "test"
        }
    }

    @Test
    internal fun `should set new money value for selected account`() {
        createAccountOwner()
        createAccount()

        accountRepository.update(Account(1, "", BigDecimal("1.23"), 1)).block()
        val account = accountRepository.findById(1).block()!!

        withClue("Account should be updated") {
            account.id shouldBe 1
            account.amount shouldBe BigDecimal("1.23")
            account.name shouldBe "test"
        }
    }

    private fun createAccount() {
        client.sql("insert into account (id, account_name,description, money, owner) values (1, 'test','desc',1.0, 1)")
            .then().block()
    }

    private fun createAccountOwner() {
        client.sql("insert into account_owner (id, description, owner_name) values (1, 'owner', 'name')").then().block()
    }

}