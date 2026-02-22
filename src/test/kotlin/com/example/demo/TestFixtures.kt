package com.example.demo

import java.math.BigDecimal

data class CatalogSeedItem(
    val assortmentId: Int,
    val assortmentName: String,
    val categoryId: Int,
    val categoryName: String
)

data class InvoiceItemSeed(
    val id: Int,
    val price: BigDecimal,
    val amount: BigDecimal,
    val unitPrice: BigDecimal,
    val discount: BigDecimal,
    val categoryId: Int,
    val assortmentId: Int
)
