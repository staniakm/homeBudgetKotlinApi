package com.example.demo.controller

import com.example.demo.service.ChartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@CrossOrigin
@RequestMapping("api/chart")
@RestController
class ChartController {

    @Autowired
    lateinit var shoppingListService: ChartService

    @GetMapping("/currentMonth")
    fun getCurrentMonthSummary() = shoppingListService.getMonthChardData(LocalDate.now().monthValue)

    @GetMapping("/previousMonth")
    fun getPreviousMonthSummary() =
            shoppingListService.getMonthChardData(LocalDate.now().minusMonths(1).monthValue)
}