package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Shop
import com.example.demo.entity.ShopItem
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ShopRepositoryTest(@Autowired private val shopRepository: ShopRepository) : IntegrationTest() {

    @Test
    fun `should get all shops`() {
        createShop(1, "Shop1")
        createShop(2, "shop2")

        val shops = shopRepository.getAllShops().collectList().block()!!

        shops.size shouldBe 2
        shops shouldContainAll listOf(Shop(1, "Shop1"), Shop(2, "shop2"))
    }

    @Test
    fun `should create new shop`() {

        val shop = shopRepository.createShop("shopName").block()!!

        shop.shopId shouldBe 1
        shop.name shouldBe "shopName"
    }

    @Test
    fun `should get shop items`() {
        createShop()
        createCategory(1)
        createAssortment(1, "aso1", 1)
        createAssortment(2, "aso2", 1)
        createAssortment(3, "aso2", 1)
        createShopItem(1, 1)
        createShopItem(1, 2)

        val items = shopRepository.getShopItems(1).collectList().block()!!

        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopItem(1, "aso1"),
            ShopItem(2, "aso2"),
        )
    }
}