package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class ShopSummary(val shopId: Int, val name: String, val monthSum: BigDecimal, val yearSum: BigDecimal)

data class Shop(val shopId: Long, val name: String)

object ShopRowMapper {
    val map: (row: Row) -> Shop = { row ->
        Shop(
            row.get("id", Long::class.java)!!,
            row.get("name", String::class.java)!!
        )
    }
}

object ShopSummaryRowMapper {
    val map: (row: Row) -> ShopSummary = { row ->
        ShopSummary(
            row.get("id", Number::class.java)!! as Int,
            row.get("nazwa", String::class.java)!!,
            row.get("monthSum", BigDecimal::class.java)!!,
            row.get("yearSum", BigDecimal::class.java)!!
        )
    }
}
