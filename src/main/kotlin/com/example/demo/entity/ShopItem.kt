package com.example.demo.entity

import java.sql.ResultSet


data class ShopItem(val itemId: Int, val name: String)

val shopItemRowMapperJdbc: (row: ResultSet, _: Any?) -> ShopItem = { row, _ ->
    ShopItem(
        row.getInt("id"),
        row.getString("name")
    )
}

data class CreateShopItemRequest(val shopId: Int, val name: String)