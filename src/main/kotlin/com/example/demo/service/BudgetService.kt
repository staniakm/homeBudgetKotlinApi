package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.MonthBudgetDto
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BudgetService {

    @Autowired
    lateinit var repository: Repository

    fun getMonthBudget(month: Long): BudgetItem {
        val date = LocalDate.now().plusMonths(month);
        return repository.getBudgetForMonth(date)
    }

    fun getMonthBudgetForCategory(month: Long, category: String): BudgetItem {
        val date = LocalDate.now().plusMonths(month)
        return repository.getBudgetForMonthAndCategory(date, category)
    }

    fun updateBudget(month: Long, monthBudget: MonthBudgetDto) {
        val date = LocalDate.now().plusMonths(month)
        repository.updateBudget(date, monthBudget)
    }

}
