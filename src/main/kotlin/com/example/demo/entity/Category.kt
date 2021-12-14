package com.example.demo.entity

import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import java.math.BigDecimal
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

val categoryRowMapper: (row: Row) -> Category = { row ->
    Category(
        row["id"] as Int,
        row["name"] as String,
        row["monthSummary"] as BigDecimal,
        row["yearSummary"] as BigDecimal,
    )
}
