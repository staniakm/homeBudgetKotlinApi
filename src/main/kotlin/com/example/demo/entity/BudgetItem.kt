package com.example.demo.entity

import java.math.BigDecimal

class BudgetItem(val date: String, val budgets: List<MonthBudget>){

    lateinit var totalSpend:BigDecimal
    lateinit var totalPlanned: BigDecimal
    lateinit var totalEarned:BigDecimal
}

data class MonthBudget(val category: String, val spent: BigDecimal, val planned: BigDecimal, val percentage: Double)