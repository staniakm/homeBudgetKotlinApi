package com.example.demo.entity

import java.math.BigDecimal

class ShoppingItem(val id: Long, val productName: String, val quantity: BigDecimal,
                   val price: BigDecimal, val unitPrice: BigDecimal, val description: String = "")