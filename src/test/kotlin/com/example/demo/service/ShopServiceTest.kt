package com.example.demo.service

import com.example.demo.IntegrationTest
import com.example.demo.entity.CreateShopRequest
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ShopServiceTest(
    @Autowired val shopService: ShopService
) : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
//        createAccountOwner()
//        createAccount()
//        createShop()
    }

    @Test
    fun `should create new shop and return its id nad name`() {
        //given input data
        val request = CreateShopRequest("shop_name")
        //when
        val shop = shopService.createShop(request)
        //then
        shop.shopId shouldBe 1
        shop.name shouldBeEqualComparingTo  "SHOP_NAME"
    }
}