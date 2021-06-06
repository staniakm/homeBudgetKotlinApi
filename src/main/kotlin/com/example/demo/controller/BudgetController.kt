package com.example.demo.controller

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.service.BudgetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
class BudgetController(private val budgetService: BudgetService) {

    @GetMapping
    fun getBudgetForMonth(@RequestParam("month") month: Long) : ResponseEntity<BudgetItem> =
            ResponseEntity(budgetService.getMonthBudget(month), HttpStatus.OK)

    @PutMapping(produces = ["application/json"])
    fun updateBudgetForMonth(@RequestParam("month") month: Long, @RequestBody updateBudget: UpdateBudgetDto): ResponseEntity<BudgetItem> {
        return ResponseEntity(budgetService.updateBudget(month, updateBudget), HttpStatus.OK)
    }
}
