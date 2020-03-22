package com.example.demo.entity

import java.math.BigDecimal

data class ShopSummary (val shopId: Long, val name: String, val monthSum: BigDecimal, val yearSum: BigDecimal)

data class Shop(val shopId: Long, val name: String)
