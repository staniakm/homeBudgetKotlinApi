package com.example.demo.entity

import java.math.BigDecimal

class BudgetItem(val date: String, val budgets: List<MonthBudget>){

    var totalSpend:BigDecimal = BigDecimal.ZERO
    var totalPlanned: BigDecimal = BigDecimal.ZERO
    var totalEarned:BigDecimal = BigDecimal.ZERO
}

data class MonthBudget(val category: String, val spent: BigDecimal, val planned: BigDecimal, val percentage: Double)