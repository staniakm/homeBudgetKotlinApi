package com.example.demo.service

import com.example.demo.IntegrationTest
import com.example.demo.entity.*
import com.example.demo.repository.AccountRepository
import com.example.demo.repository.InvoiceRepository
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.LocalDate

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