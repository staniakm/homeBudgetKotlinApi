package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.Account
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal

class AccountControllerTest : IntegrationTest() {

    @Test
    fun `should geturn empty list when no accounts exists`() {
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
}