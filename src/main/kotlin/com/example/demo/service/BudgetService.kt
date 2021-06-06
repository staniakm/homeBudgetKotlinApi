package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.repository.BudgetRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BudgetService(private val repository: BudgetRepository) {

    fun getMonthBudget(month: Long): BudgetItem? {
        return LocalDate.now().plusMonths(month)
                .let {
                    repository.getBudgetForMonth(it)
                }
    }

    fun getMonthBudgetForCategory(month: Long, category: String): BudgetItem {
        val date = LocalDate.now().plusMonths(month)
        return repository.getBudgetForMonthAndCategory(date, category)
    }

    fun updateBudget(month: Long, updateBudget: UpdateBudgetDto): BudgetItem {
        return LocalDate.now().plusMonths(month)
                .also {
                    repository.updateBudget(it, updateBudget)
                }.apply {
                    repository.recalculateBudget(this)
                }.let {
                    getMonthBudgetForCategory(month, updateBudget.category)
                }
    }

}
