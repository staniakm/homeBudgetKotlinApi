package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.ProductHistory
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class ItemControllerTest : IntegrationTest() {

    @Test
    fun `should return product history for selected item`() {
        createAccountOwner()
        createAccount()
        createShop()
        createCategory(1, "Food")
        createAssortment(1, "Milk", 1)
        createShopItem(1, 1)
        createInvoice(1, 1, LocalDate.of(2022, 5, 10), BigDecimal("20.00"), 1)
        createInvoiceItem(1, 1, BigDecimal("10.00"), BigDecimal("2.000"), BigDecimal("5.00"), BigDecimal.ZERO, 1, 1)

        val response = restTemplate.getForEntity("/api/item/1", Array<ProductHistory>::class.java)

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 1
        with(response.body!!.first()) {
            shopName shouldBe "ShopName"
            productName shouldBe "Milk"
            invoiceId shouldBe 1
        }
    }

    @Test
    fun `should update product category`() {
        createCategory(1, "Food")
        createCategory(2, "Home")
        createAssortment(1, "Milk", 1)

        val update = restTemplate.exchange(
            "/api/item/1/update-category?categoryId=2",
            HttpMethod.PUT,
            null,
            Void::class.java
        )

        update.statusCode shouldBe HttpStatus.OK

        val assortment = restTemplate.getForEntity("/api/assortment/1", AssortmentDetailsResponse::class.java)
        assortment.statusCode shouldBe HttpStatus.OK
        assortment.body?.categoryId shouldBe 2
    }
}
