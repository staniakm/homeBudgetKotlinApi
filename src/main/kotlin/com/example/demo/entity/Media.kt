package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata

data class MediaTypeRequest(val mediaName: String)

data class MediaType(val id: Int, val name: String)

val mediaTypeMapper: (row: Row, metadata: RowMetadata) -> MediaType = { row ,_->
    MediaType(
        row["id"] as Int,
        row["name"] as String
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

val mediaUsageMapper = { row: Row, _:RowMetadata ->
    MediaUsage(
        row["id"] as Int,
        row["type_id"] as Int,
        row["year"] as Int,
        row["month"] as Int,
        row["meter_read"] as Double
    )
}

data class MediaItem(val id: Int, val mediaType: Int, val year: Int, val month: Int, val meterRead: Double)

val mediaMapper: (row: Row, metadata:RowMetadata) -> MediaItem = { row,_ ->
    MediaItem(
        row["id"] as Int,
        row["media_type"] as Int,
        row["year"] as Int,
        row["month"] as Int,
        row["meter_read"] as Double
    )
}