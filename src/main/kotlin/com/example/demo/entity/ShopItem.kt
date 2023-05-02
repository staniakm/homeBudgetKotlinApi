package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.sql.ResultSet


data class ShopItem(val itemId: Int, val name: String)

val shopItemRowMapper: (row: Row, metadata: RowMetadata) -> ShopItem = { row, _ ->
    ShopItem(
        row["id"] as Int,
        row["name"] as String,
    )
}


val shopItemRowMapperJdbc: (row: ResultSet, _: Any?) -> ShopItem = { row, _ ->
    ShopItem(
        row.getInt("id"),
        row.getString("name")
    )
}

data class CreateShopItemRequest(val shopId: Int, val name: String)