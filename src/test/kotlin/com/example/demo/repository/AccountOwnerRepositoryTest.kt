package com.example.demo.repository

import com.example.demo.IntegrationTest
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class AccountOwnerRepositoryTest(@Autowired private val accountOwnerRepository: AccountOwnerRepository) :
    IntegrationTest() {

    @Test
    fun `should get all owners`() {
        createAccountOwner(1, "Owner1")
        createAccountOwner(2, "owner2")
        createAccountOwner(3, "owner3")

        val findAllAccounts = accountOwnerRepository.findAllOwners()

        with(findAllAccounts) {
            size shouldBe 3
            map { it.name } shouldContainAll listOf("Owner1", "owner2", "owner3")
        }
    }

    @Test
    fun `should create new account owner`() {
        val owner = accountOwnerRepository.createNewOwner("Jan", "Father")
        owner?.name shouldBe "JAN"
        owner?.description shouldBe "Father"
    }

    @Test
    fun `should return null when owner already exists`() {
        createAccountOwner(1, "Owner1")

        val owner = accountOwnerRepository.createNewOwner("Owner1", "Father")

        owner shouldBe null
    }
}
