package com.example.demo.entity

import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate

data class Category(
    val id: Int, val name: String, val monthSummary: BigDecimal,
    val yearSummary: BigDecimal
) {
    companion object {
        val bindByDate: (LocalDate, DatabaseClient.GenericExecuteSpec) -> DatabaseClient.GenericExecuteSpec =
            { date, query ->
                query.bind("year", date.year)
                    .bind("year2", date.year)
                    .bind("month", date.monthValue)
            }
    }
}

object CategoryRowMapper {
    val map: (row: Row) -> Category = { row ->
        Category(
            row.get("id", Number::class.java)!! as Int,
            row.get("nazwa", String::class.java)!!,
            row.get("monthSummary", BigDecimal::class.java)!!,
            row.get("yearSummary", BigDecimal::class.java)!!,
        )
    }
}
