package com.example.demo

import com.example.demo.service.ClockProviderInterface
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class FakeClockProvider(private var date: String = "2021-01-01T00:00:00Z") : ClockProviderInterface {
    override fun getTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.parse(date), ZoneId.of("GMT+2"))
    }

    override fun getDate(): LocalDate {
        return LocalDate.ofInstant(Instant.parse(date), ZoneId.of("GMT+2"))
    }

    fun setTime(timestamp: String) {
        date = timestamp
    }

    fun resetTime() {
        date = "2021-01-01T00:00:00Z"
    }

    override val getDateFromMonth: (Long) -> LocalDate
        get() = { month ->
            getDate().plusMonths(month)
        }

}
