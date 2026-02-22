package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.AccountOwner
import com.example.demo.entity.CreateOwnerRequest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class OwnerControllerTest : IntegrationTest() {

    @Test
    fun `should get empty list while fetching all owners when no owners exist`() {
        val findAllOwners = restTemplate.getForEntity("/api/owner/", Array<AccountOwner>::class.java)

        findAllOwners.statusCode.is2xxSuccessful
        findAllOwners.body!!.size shouldBe 0
    }

    @Test
    fun `should fetch list of existing owners`() {
        testDataBuilder.accountOwner(1, "owner1")
        testDataBuilder.accountOwner(2, "owner2")
        testDataBuilder.accountOwner(3, "owner3")

        val findAllOwners = restTemplate.getForEntity("/api/owner/", Array<AccountOwner>::class.java)

        findAllOwners.statusCode.is2xxSuccessful
        findAllOwners.body!!.size shouldBe 3
        with(findAllOwners.body!!.asList()){
            this[0].id shouldBe 1
            this[0].name shouldBe "owner1"
            this[1].id shouldBe 2
            this[1].name shouldBe "owner2"
            this[2].id shouldBe 3
            this[2].name shouldBe "owner3"
        }
    }

    @Test
    fun `should create new owner`() {
        val request = CreateOwnerRequest("owner1", "description1")
        val response = restTemplate.postForEntity("/api/owner/", request, AccountOwner::class.java)

        response.statusCode.is2xxSuccessful
        with(response.body!!){
            id shouldBe 1
            name shouldBe "OWNER1"
            description shouldBe "description1"
        }

        val findAllOwners = restTemplate.getForEntity("/api/owner/", Array<AccountOwner>::class.java)
        findAllOwners.statusCode.is2xxSuccessful
        findAllOwners.body!!.size shouldBe 1
        with(findAllOwners.body!!.asList()){
            this[0].id shouldBe 1
            this[0].name shouldBe "OWNER1"
            this[0].description shouldBe "description1"
        }
    }

    @Test
    fun `should return bad request when try to create new owner with the same name`() {
        testDataBuilder.accountOwner(1, "OWNER1", "first owner")
        val request = CreateOwnerRequest("owner1", "description2")
        val response = restTemplate.postForEntity("/api/owner/", request, AccountOwner::class.java)

        response.statusCode shouldBe HttpStatus.BAD_REQUEST

        val findAllOwners = restTemplate.getForEntity("/api/owner/", Array<AccountOwner>::class.java)
        findAllOwners.statusCode.is2xxSuccessful
        findAllOwners.body!!.size shouldBe 1
        with(findAllOwners.body!!.asList()){
            this[0].id shouldBe 1
            this[0].name shouldBe "OWNER1"
            this[0].description shouldBe "first owner"
        }
    }
}
