package com.example.demo.controller

import com.example.demo.entity.InvoiceItem
import com.example.demo.entity.MonthBudget
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.service.BudgetService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
@RequestMapping("/api/budget")
class BudgetController(private val budgetService: BudgetService) {

    @GetMapping
    fun getBudgetForMonth(@RequestParam("month", required = false, defaultValue = "0") month: Long): Mono<MonthBudget> =
        budgetService.getMonthBudget(month)

    @GetMapping("/{budgetId}")
    fun getBudgetItem(
        @PathVariable budgetId: Int
    ): Flux<InvoiceItem> = budgetService.getBudgetItem(budgetId)

    @PutMapping(produces = ["application/json"])
    fun updateBudgetForMonth(@RequestBody updateBudget: UpdateBudgetDto) = budgetService.updateBudget(updateBudget)

    @PutMapping("/recalculate")
    fun recalculateBudget(@RequestParam("month", required = false, defaultValue = "0") month: Long): Mono<MonthBudget> =
        budgetService.recalculateBudgets(month)
}
