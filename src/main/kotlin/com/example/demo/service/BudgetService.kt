package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.MonthBudgetDto
import com.example.demo.repository.BudgetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BudgetService(private val repository: BudgetRepository) {

    fun getMonthBudget(month: Long): BudgetItem {
        return LocalDate.now().plusMonths(month)
                .let {
                    repository.getBudgetForMonth(it)
                }
    }

    fun getMonthBudgetForCategory(month: Long, category: String): BudgetItem {
        val date = LocalDate.now().plusMonths(month)
        return repository.getBudgetForMonthAndCategory(date, category)
    }

    fun updateBudget(month: Long, monthBudget: MonthBudgetDto): BudgetItem {
        return LocalDate.now().plusMonths(month)
                .also {
                    repository.updateBudget(it, monthBudget)
                }.apply {
                    repository.recalculateBudget(this)
                }.let {
                    getMonthBudgetForCategory(month, monthBudget.category)
                }
    }

}
