package com.example.demo.entity

import io.r2dbc.spi.Row
import java.math.BigDecimal

data class CategoryDetails(val name: String, val price: BigDecimal)

object CategoryDetailsRowMapper {
    val map: (row: Row) -> CategoryDetails = { row ->
        CategoryDetails(
            row.get("nazwa", String::class.java)!!,
            row.get("cena", BigDecimal::class.java)!!
        )
    }
}
