package com.example.demo.entity

import java.math.BigDecimal

class ShopItemsSummary(val itemId: Long, val productName: String, val quantity: BigDecimal,
                       val minPrice: BigDecimal, val maxPrice: BigDecimal,
                       val totalDiscount: BigDecimal, val totalSpend: BigDecimal)