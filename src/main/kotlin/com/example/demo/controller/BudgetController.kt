package com.example.demo.controller

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.service.BudgetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
class BudgetController(private val budgetService: BudgetService) {

    @GetMapping
    fun getBudgetForMonth(@RequestParam("month", required = false, defaultValue = "0") month: Long): Mono<BudgetItem> =
        budgetService.getMonthBudget(month)

    @PutMapping(produces = ["application/json"])
    fun updateBudgetForMonth(@RequestParam("month", defaultValue = "0") month: Long, @RequestBody updateBudget: UpdateBudgetDto) =
        budgetService.updateBudget(month, updateBudget)
}
