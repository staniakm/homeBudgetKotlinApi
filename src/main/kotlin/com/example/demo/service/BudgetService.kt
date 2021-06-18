package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.entity.UpdateBudgetDto
import com.example.demo.repository.BudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class BudgetService(private val repository: BudgetRepository, private val clock: ClockProvider) {

    fun getMonthBudget(month: Long) = repository.getBudgetForMonth(clock.getDateFromMonth(month))

    @Transactional
    fun updateBudget(month: Long, updateBudget: UpdateBudgetDto):Mono<BudgetItem> {
        return clock.getDateFromMonth(month)
            .let {
                repository.updateBudget(it, updateBudget)
                repository.getBudgetForMonthAndCategory(it, updateBudget.category)
//                repository.recalculateBudget(it)
            }
    }
}
