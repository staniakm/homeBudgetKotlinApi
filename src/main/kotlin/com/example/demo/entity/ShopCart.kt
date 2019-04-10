package com.example.demo.entity

import java.math.BigDecimal

class ShopCartDetails (val itemId: Long, val productName: String, val quantity: BigDecimal,
                val price: BigDecimal, val discount: BigDecimal, val totalPrice: BigDecimal)