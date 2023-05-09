package com.example.demo.config

import com.example.demo.FakeClockProvider
import com.example.demo.service.ClockProviderInterface
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {

    @Bean
    @Primary
    fun fakeClockProvider(): ClockProviderInterface {
        return FakeClockProvider()
    }
}