package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDate

data class Category(
    val id: Int, val name: String, val monthSummary: BigDecimal,
    val yearSummary: BigDecimal
) {
    companion object {
        val bindByDate: (LocalDate, DatabaseClient.GenericExecuteSpec) -> DatabaseClient.GenericExecuteSpec =
            { date, query ->
                query.bind("$1", date.year)
                    .bind("$2", date.monthValue)
            }
    }
}

val categoryRowMapper: (row: ResultSet, _: Any?) -> Category = { row,_ ->
    Category(
        row.getInt("id") as Int,
        row.getString("name") as String,
        row.getBigDecimal("monthSummary") as BigDecimal,
        row.getBigDecimal("yearSummary") as BigDecimal,
    )
}
