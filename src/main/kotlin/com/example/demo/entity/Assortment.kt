package com.example.demo.entity

import java.sql.ResultSet


data class Assortment(val id: Long, val name: String, val categoryId: Long)

val assortmentSingleItemMapper: (row: ResultSet) -> Assortment? = { row ->
    if (row.next()) {
        Assortment(
            row.getLong("id"),
            row.getString("name") as String,
            row.getLong("category"),
        )
    } else {
        null
    }
}
