package com.example.demo.entity

import io.r2dbc.spi.Row
import org.springframework.cglib.core.Local
import java.math.BigDecimal
import java.time.LocalDate

data class Invoice(
    val id: Long,
    val date: LocalDate,
    val invoiceNumber: String,
    val sum: BigDecimal,
    val description: String,
    val del: Boolean,
    val account: Int,
    val shop: Int
)

val invoiceRowMapper: (row: Row) -> Invoice = { row ->
    Invoice(
        row["id"] as Long,
        row["date"] as LocalDate,
        row["invoice_number"] as String,
        row["sum"] as BigDecimal,
        row["description"] as String,
        row["del"] as Boolean,
        row["account"] as Int,
        row["shop"] as Int
    )
}
