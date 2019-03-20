package com.example.demo.controller

import com.example.demo.entity.ChartData
import com.example.demo.service.ShoppingListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RequestMapping("api/chart")
@RestController
class ChartController {

    @Autowired
    lateinit var shoppingListService : ShoppingListService

    @GetMapping("/currentMonth")
    fun getCurrentMonthSummary():List<ChartData>{
        return shoppingListService.getMontchChardData()
    }
}