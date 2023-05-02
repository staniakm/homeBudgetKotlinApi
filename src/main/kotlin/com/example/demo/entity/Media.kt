package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.sql.ResultSet

data class MediaTypeRequest(val mediaName: String)

data class MediaType(val id: Int, val name: String)

val mediaTypeMapper: (row: ResultSet, _:Any?) -> MediaType = { row, _ ->
    MediaType(
        row.getInt("id") as Int,
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
        row.getInt("id") as Int,
        row.getInt("type_id") as Int,
        row.getInt("year") as Int,
        row.getInt("month") as Int,
        row.getDouble("meter_read") as Double
    )
}

data class MediaItem(val id: Int, val mediaType: Int, val year: Int, val month: Int, val meterRead: Double)

val mediaMapper: (row: ResultSet, _: Any?) -> MediaItem = { row, _ ->
    MediaItem(
        row.getInt("id") as Int,
        row.getInt("media_type") as Int,
        row.getInt("year") as Int,
        row.getInt("month") as Int,
        row.getDouble("meter_read") as Double
    )
}