package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.MonthBudgetPlanned
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.repository.BudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class BudgetService(private val repository: BudgetRepository, private val clock: ClockProvider) {

    fun getMonthBudget(month: Long) = repository.getBudgetForMonth(clock.getDateFromMonth(month))

    @Transactional
    fun updateBudget(updateBudget: UpdateBudgetDto): Mono<MonthBudgetPlanned> {
        return repository.updateBudget(updateBudget)
            .then(repository.getSelectedBudgetItem(updateBudget.budgetId))
    }

    fun recalculateBudgets(month: Long): Mono<BudgetItem> {
        return repository.recalcualteBudgets(clock.getDateFromMonth(month))
            .then(getMonthBudget(month))
    }
}
