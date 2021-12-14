package com.example.demo.controller

import com.example.demo.entity.ChartData
import com.example.demo.service.ChartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.Month

@CrossOrigin
@RequestMapping("api/chart")
@RestController
class ChartController(private val shoppingListService: ChartService) {

    @GetMapping("")
    fun getCurrentMonthSummary(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<Flux<ChartData>> {
        val monthValue = LocalDate.now().plusMonths(month);
        return ResponseEntity(shoppingListService.getMonthChardData(monthValue), HttpStatus.OK)
    }
}
