package com.example.demo.entity

import io.r2dbc.spi.Row

data class MediaTypeRequest(val mediaName: String)

data class MediaType(val id: Int, val name: String)

val mediaTypeMapper: (row: Row) -> MediaType = { row ->
    MediaType(
        row["id"] as Int,
        row["name"] as String
    )
}