package com.example.demo.entity

import io.r2dbc.spi.Row


data class ShopItem(val itemId: Long, val name: String)

object ShopItemRowMapper {
    val map: (row: Row) -> ShopItem = { row ->
        ShopItem(
            row.get("id", Long::class.java)!!,
            row.get("nazwa", String::class.java)!!
        )
    }
}
