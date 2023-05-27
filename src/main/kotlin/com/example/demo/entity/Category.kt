package com.example.demo.entity

import java.sql.ResultSet

data class Category(val id: Int, val name: String)

val  categorySingleItemMapper: (row: ResultSet) -> Category? = { row ->
    if (row.next()) {
        Category(
            row.getInt("id"),
            row.getString("name") as String,
        )
    } else {
        null
    }
}