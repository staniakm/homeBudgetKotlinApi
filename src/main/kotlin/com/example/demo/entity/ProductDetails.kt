package com.example.demo.entity

import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDate

data class ProductDetails (val shopName: String, val invoiceDate: Date, val itemPrice: BigDecimal,
                           val quantity: Double, val discount: BigDecimal, val totalSum: BigDecimal, val invoiceId: Long,
                           val invoiceItemId: Long)