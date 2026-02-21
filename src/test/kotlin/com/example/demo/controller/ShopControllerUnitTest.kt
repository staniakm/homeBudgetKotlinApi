package com.example.demo.controller

import com.example.demo.entity.ShopItemsSummary
import com.example.demo.service.ShopService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class ShopControllerUnitTest {

    @Test
    fun `should call year details endpoint with default month argument`() {
        val service = mock(ShopService::class.java)
        val expected = listOf(
            ShopItemsSummary(1, "Milk", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE)
        )
        `when`(service.getYearShopItemsSummary(1, 0L)).thenReturn(expected)

        val controller = ShopController(service)
        val response = controller.getShopYearDetails(1)

        response.statusCode.value() shouldBe 200
        response.body shouldBe expected
    }
}
