package com.example.demo.entity

import java.sql.ResultSet

data class MediaTypeRequest(val mediaName: String)

data class MediaType(val id: Int, val name: String)

val mediaTypeMapper: (row: ResultSet, _: Any?) -> MediaType = { row, _ ->
    MediaType(
        row.getInt("id"),
        row.getString("name") as String
    )
}

data class MediaRegisterRequest(val mediaType: Int, val meterRead: Double, val year: Int, val month: Int)
data class MediaUsageResponse(val id: Int, val year: Int, val month: Int, val meterRead: Double)
data class MediaUsage(
    val id: Int,
    val mediaTypeId: Int,
    val year: Int,
    val month: Int,
    val lastRead: Double
) {
    fun toResponse(): MediaUsageResponse {
        return MediaUsageResponse(id, year, month, lastRead)
    }
}

val mediaUsageMapper = { row: ResultSet, _: Any? ->
    MediaUsage(
        row.getInt("id"),
        row.getInt("type_id"),
        row.getInt("year"),
        row.getInt("month"),
        row.getDouble("meter_read")
    )
}

data class MediaItem(val id: Int, val mediaType: Int, val year: Int, val month: Int, val meterRead: Double)

val mediaMapper: (row: ResultSet, _: Any?) -> MediaItem = { row, _ ->
    MediaItem(
        row.getInt("id"),
        row.getInt("media_type"),
        row.getInt("year"),
        row.getInt("month"),
        row.getDouble("meter_read")
    )
}