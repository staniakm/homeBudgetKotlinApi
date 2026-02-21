package com.example.demo.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ClockProviderTest {

    private val clock = ClockProvider()

    @Test
    fun `should return date shifted by requested months`() {
        val today = clock.getDate()

        clock.getDateFromMonth(0) shouldBe today
        clock.getDateFromMonth(3) shouldBe today.plusMonths(3)
        clock.getDateFromMonth(-2) shouldBe today.minusMonths(2)
    }

    @Test
    fun `should return current date and time`() {
        clock.getDate() shouldBe clock.getTime().toLocalDate()
    }
}
