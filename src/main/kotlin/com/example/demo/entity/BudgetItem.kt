package com.example.demo.entity

import java.math.BigDecimal
import java.time.LocalDate

data class BudgetItem(val date: String, val budgets: List<MonthBudget>)

data class MonthBudget(val category: String, val spent: BigDecimal, val planned: BigDecimal, val percentage: Double)