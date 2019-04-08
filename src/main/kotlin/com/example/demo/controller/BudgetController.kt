package com.example.demo.controller

import com.example.demo.service.BudgetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
class BudgetController {

    @Autowired
    lateinit var budgetService: BudgetService

    @GetMapping
    fun getBudgetForMonth(@RequestParam("month") month: Long) =
            ResponseEntity(budgetService.getMonthBudget(month), HttpStatus.OK)

}
