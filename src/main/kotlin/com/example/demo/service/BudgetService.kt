package com.example.demo.service

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
            .then(repository.recalculateBudget(updateBudget.budgetId))
            .then(repository.getSelectedBudgetItem(updateBudget.budgetId))
    }
}
