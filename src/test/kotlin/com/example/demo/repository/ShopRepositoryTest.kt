package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.Shop
import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

class ShopRepositoryTest(@Autowired private val shopRepository: ShopRepository) : IntegrationTest() {

    @Test
    fun `should get all shops`() {
        testDataBuilder.shop(1, "Shop1")
        testDataBuilder.shop(2, "shop2")

        val shops = shopRepository.getAllShops()

        shops.size shouldBe 2
        shops shouldContainAll listOf(Shop(1, "Shop1"), Shop(2, "shop2"))
    }

    @Test
    fun `should create new shop`() {

        val shop = shopRepository.createShop("shopName")

        shop.shopId shouldBe 1
        shop.name shouldBe "Shopname"

        val shop2 = shopRepository.createShop("shopName2")

        shop2.shopId shouldBe 2
        shop2.name shouldBe "Shopname2"
    }

    @Test
    fun `should get shop items`() {
        testDataBuilder.shop()
        testDataBuilder.category(1)
        testDataBuilder.assortment(1, "aso1", 1)
        testDataBuilder.assortment(2, "aso2", 1)
        testDataBuilder.assortment(3, "aso2", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)

        val items = shopRepository.getShopItems(1)

        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopItem(1, "aso1"),
            ShopItem(2, "aso2"),
        )
    }

    @Test
    fun `should create shop item`() {
        testDataBuilder.shop()
        testDataBuilder.category(1)
        testDataBuilder.assortment(1, "aso1", 1)
        testDataBuilder.assortment(2, "aso2", 1)
        testDataBuilder.assortment(3, "aso3", 1)

        val item = shopRepository.createShopItem(1, "aso2")

        item.itemId shouldBe 2
        item.name shouldBe "aso2"

        val item2 = shopRepository.createShopItem(1, "aso3")

        item2.itemId shouldBe 3
        item2.name shouldBe "aso3"
    }

    @Test
    fun `should fetch shop year items summary`() {
        testDataBuilder.shop()
        testDataBuilder.accountOwner(1)
        testDataBuilder.account(1)
        testDataBuilder.category(1)
        testDataBuilder.assortment(1, "aso1", 1)
        testDataBuilder.assortment(2, "aso2", 1)
        testDataBuilder.assortment(3, "aso3", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)
        testDataBuilder.shopItem(1, 3)

        testDataBuilder.invoice(1, 1, LocalDate.of(2021, 1, 2), BigDecimal(10))
        testDataBuilder.invoiceItem(1, 1, BigDecimal(10.4), BigDecimal(2), BigDecimal(5.2), BigDecimal(1), 1, 1)
        testDataBuilder.invoiceItem(2, 1, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 2)

        testDataBuilder.invoice(2, 1, LocalDate.of(2021, 4, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(3, 2, BigDecimal(21.6), BigDecimal(4), BigDecimal(5.4), BigDecimal(1), 1, 2)

        testDataBuilder.invoice(3, 1, LocalDate.of(2020, 2, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(4, 3, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 3)

        testDataBuilder.invoice(4, 1, LocalDate.of(2020, 2, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(5, 4, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 1)

        val items = shopRepository.getShopYearItems(1, LocalDate.of(2021, 2, 1))

        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopItemsSummary(
                1, "aso1", BigDecimal("2.000"), BigDecimal("5.20"), BigDecimal("5.20"), BigDecimal("1.00"),
                BigDecimal("10.40")
            ),
            ShopItemsSummary(
                2,
                "aso2",
                BigDecimal("8.000"),
                BigDecimal("5.10"),
                BigDecimal("5.40"),
                BigDecimal("2.00"),
                BigDecimal("42.00")
            )
        )
    }

    @Test
    fun `should fetch shop month items summary`() {
        testDataBuilder.shop()
        testDataBuilder.accountOwner(1)
        testDataBuilder.account(1)
        testDataBuilder.category(1)
        testDataBuilder.assortment(1, "aso1", 1)
        testDataBuilder.assortment(2, "aso2", 1)
        testDataBuilder.assortment(3, "aso3", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)
        testDataBuilder.shopItem(1, 3)

        testDataBuilder.invoice(1, 1, LocalDate.of(2021, 1, 2), BigDecimal(10))
        testDataBuilder.invoiceItem(1, 1, BigDecimal(10.4), BigDecimal(2), BigDecimal(5.2), BigDecimal(1), 1, 1)
        testDataBuilder.invoiceItem(2, 1, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 2)

        testDataBuilder.invoice(2, 1, LocalDate.of(2021, 4, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(3, 2, BigDecimal(21.6), BigDecimal(4), BigDecimal(5.4), BigDecimal(1), 1, 2)

        testDataBuilder.invoice(3, 1, LocalDate.of(2020, 2, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(4, 3, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 3)
        testDataBuilder.invoiceItem(6, 3, BigDecimal(8.2), BigDecimal(2), BigDecimal(4.1), BigDecimal(2.15), 1, 1)

        testDataBuilder.invoice(4, 1, LocalDate.of(2020, 2, 1), BigDecimal(10))
        testDataBuilder.invoiceItem(5, 4, BigDecimal(20.4), BigDecimal(4), BigDecimal(5.1), BigDecimal(1), 1, 1)

        val items = shopRepository.getShopMonthItems(1, LocalDate.of(2020, 2, 1))

        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopItemsSummary(
                1, "aso1", BigDecimal("6.000"), BigDecimal("4.10"), BigDecimal("5.10"), BigDecimal("3.15"),
                BigDecimal("28.60")
            ),
            ShopItemsSummary(
                3,
                "aso3",
                BigDecimal("4.000"),
                BigDecimal("5.10"),
                BigDecimal("5.10"),
                BigDecimal("1.00"),
                BigDecimal("20.40")
            )
        )
    }

    @Test
    fun `should get all shops month summary`() {
        testDataBuilder.shop()
        testDataBuilder.shop(2, "shop2")
        testDataBuilder.shop(3, "shop3")
        testDataBuilder.accountOwner(1)
        testDataBuilder.account(1)
        testDataBuilder.category(1)
        testDataBuilder.assortment(1, "aso1", 1)
        testDataBuilder.assortment(2, "aso2", 1)
        testDataBuilder.assortment(3, "aso3", 1)
        testDataBuilder.shopItem(1, 1)
        testDataBuilder.shopItem(1, 2)
        testDataBuilder.shopItem(1, 3)

        //shop 1 invoice
        testDataBuilder.invoice(1, 1, LocalDate.of(2021, 1, 2), BigDecimal(10.11), 1)

        //shop 1 invoice
        testDataBuilder.invoice(3, 1, LocalDate.of(2021, 1, 10), BigDecimal(20.24), 1)

        //shop 1 invoice out of date
        testDataBuilder.invoice(4, 1, LocalDate.of(2021, 2, 10), BigDecimal(15.77), 1)

        //shop 2 invoice
        testDataBuilder.invoice(2, 1, LocalDate.of(2021, 1, 2), BigDecimal(16.66), 2)


        val items = shopRepository.getAllShopsMonthSummary(LocalDate.of(2021, 1, 1))

        items.size shouldBe 2
        items shouldContainAll listOf(
            ShopSummary(1, "ShopName", monthSum = BigDecimal("30.35"), yearSum = BigDecimal("46.12")),
            ShopSummary(2, "shop2", monthSum = BigDecimal("16.66"), yearSum = BigDecimal("16.66")),
        )

    }
}