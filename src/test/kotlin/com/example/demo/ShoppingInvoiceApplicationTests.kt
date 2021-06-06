package com.example.demo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ShoppingInvoiceApplicationTests() {

    @Autowired
    private lateinit var jdbiTemplate: Jdbi

    @Test
    fun contextLoads() {
    }
}
