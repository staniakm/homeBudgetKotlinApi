package com.example.demo.service

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class ClockProvider {

    fun getTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+2"))
    }

    fun getDate(): LocalDate {
        return LocalDate.now()
    }

    val getDateFromMonth: (Long) -> LocalDate = { month ->
        getDate()
            .plusMonths(month)
    }}
