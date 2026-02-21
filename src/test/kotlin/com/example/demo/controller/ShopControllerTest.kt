package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.CreateShopItemRequest
import com.example.demo.entity.CreateShopRequest
import com.example.demo.entity.Shop
import com.example.demo.entity.ShopItem
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ShopControllerTest : IntegrationTest() {

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
}
