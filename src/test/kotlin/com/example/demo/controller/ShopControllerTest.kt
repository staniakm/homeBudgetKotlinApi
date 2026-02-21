package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.CreateShopItemRequest
import com.example.demo.entity.CreateShopRequest
import com.example.demo.entity.Shop
import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class ShopControllerTest : IntegrationTest() {

    @Test
    fun `should return month shop summaries`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "ShopOne")
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("25.00"), 1)

        val response = restTemplate.getForEntity("/api/shop?month=0", Array<ShopSummary>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 1
        response.body?.first()?.shopId shouldBe 1
    }

    @Test
    fun `should return empty shops list`() {
        val response = restTemplate.getForEntity("/api/shop/all", Array<Shop>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0
    }

    @Test
    fun `should create shop and return it on list endpoint`() {
        val created = restTemplate.postForEntity("/api/shop", CreateShopRequest("mArKeT"), Shop::class.java)

        created.statusCode shouldBe HttpStatus.OK
        created.body shouldBe Shop(1, "Market")

        val all = restTemplate.getForEntity("/api/shop/all", Array<Shop>::class.java)
        all.statusCode shouldBe HttpStatus.OK
        all.body?.toList() shouldBe listOf(Shop(1, "Market"))
    }

    @Test
    fun `should create new shop item`() {
        createShop(1, "ShopName")
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)

        val createdItem = restTemplate.postForEntity(
            "/api/shop/newItem",
            CreateShopItemRequest(shopId = 1, name = "milk"),
            ShopItem::class.java
        )

        createdItem.statusCode shouldBe HttpStatus.OK
        createdItem.body shouldBe ShopItem(1, "Milk")

        val shopItems = restTemplate.getForEntity("/api/shop/1", Array<ShopItem>::class.java)
        shopItems.statusCode shouldBe HttpStatus.OK
        shopItems.body?.toList() shouldBe listOf(ShopItem(1, "Milk"))
    }

    @Test
    fun `should return month and year shop details`() {
        clockProvider.setTime("2022-05-01T00:00:00.00Z")
        createAccountOwner(1, "owner1")
        createAccount(1, BigDecimal("100.00"), "account1")
        createShop(1, "ShopName")
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)
        createShopItem(1, 1)
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("10.00"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal("10.00"), BigDecimal.ZERO, 1, 1)

        val month = restTemplate.getForEntity("/api/shop/1/month?month=0", Array<ShopItemsSummary>::class.java)
        month.statusCode shouldBe HttpStatus.OK
        month.body?.size shouldBe 1
        month.body?.first()?.productName shouldBe "Milk"

        val year = restTemplate.getForEntity("/api/shop/1/year?month=0", Array<ShopItemsSummary>::class.java)
        year.statusCode shouldBe HttpStatus.OK
        year.body?.size shouldBe 1
        year.body?.first()?.itemId shouldBe 1
    }
}
