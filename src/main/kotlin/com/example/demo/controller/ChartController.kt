package com.example.demo.controller

import com.example.demo.entity.ChartData
import com.example.demo.service.ClockProviderInterface
import com.example.demo.service.ChartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("api/chart")
@RestController
class ChartController(
    private val shoppingListService: ChartService,
    private val clockProvider: ClockProviderInterface
) {

    @GetMapping("")
    fun getCurrentMonthSummary(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<List<ChartData>> {
        val monthValue = clockProvider.getDateFromMonth(month)
        return ResponseEntity(shoppingListService.getMonthChardData(monthValue), HttpStatus.OK)
    }
}
