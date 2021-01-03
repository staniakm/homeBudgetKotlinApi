package com.example.demo.controller

import com.example.demo.entity.ChartData
import com.example.demo.service.ChartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@CrossOrigin
@RequestMapping("api/chart")
@RestController
class ChartController(private val shoppingListService: ChartService) {

    @GetMapping("/currentMonth")
    fun getCurrentMonthSummary(): ResponseEntity<List<ChartData>> {
        val monthValue = LocalDate.now();
        return ResponseEntity(shoppingListService.getMonthChardData(monthValue), HttpStatus.OK)
    }

    @GetMapping("/previousMonth")
    fun getPreviousMonthSummary(): ResponseEntity<List<ChartData>> {
        val monthValue = LocalDate.now().minusMonths(1)
        return ResponseEntity(shoppingListService.getMonthChardData(monthValue), HttpStatus.OK)
    }
}
