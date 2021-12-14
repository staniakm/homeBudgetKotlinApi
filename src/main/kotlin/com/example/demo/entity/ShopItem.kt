package com.example.demo.entity

import io.r2dbc.spi.Row


data class ShopItem(val itemId: Int, val name: String)

val shopItemRowMapper: (row: Row) -> ShopItem = { row ->
    ShopItem(
        row["id"] as  Int,
        row["name"] as  String,
    )
}
