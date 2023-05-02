package com.example.demo.controller

import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.MonthBudget
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.service.BudgetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
class BudgetController(private val budgetService: BudgetService) {

    @GetMapping
    fun getBudgetForMonth(
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<MonthBudget> =
        ResponseEntity.ok(budgetService.getMonthBudget(month))

    @GetMapping("/{budgetId}")
    fun getBudgetItem(
        @PathVariable budgetId: Int
    ): ResponseEntity<List<InvoiceItem>> = ResponseEntity.ok(budgetService.getBudgetItem(budgetId))

    @PutMapping(produces = ["application/json"])
    fun updateBudgetForMonth(@RequestBody updateBudget: UpdateBudgetDto) = budgetService.updateBudget(updateBudget)

    @PutMapping("/recalculate")
    fun recalculateBudget(
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<MonthBudget> =
        ResponseEntity.ok(budgetService.recalculateBudgets(month))
}
