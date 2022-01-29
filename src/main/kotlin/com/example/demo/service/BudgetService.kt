package com.example.demo.service

import com.example.demo.entity.*
import com.example.demo.repository.BudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class BudgetService(
    private val repository: BudgetRepository,
    private val invoiceService: InvoiceService,
    private val clock: ClockProvider
) {

    fun getMonthBudget(month: Long) = repository.getBudgetForMonth(clock.getDateFromMonth(month))

    @Transactional
    fun updateBudget(updateBudget: UpdateBudgetDto): Mono<MonthBudgetPlanned> {
        return repository.updateBudget(updateBudget)
            .then(repository.getSelectedBudgetItem(updateBudget.budgetId))
    }

    fun recalculateBudgets(month: Long): Mono<MonthBudget> {
        return repository.recalculateBudgets(clock.getDateFromMonth(month))
            .then(getMonthBudget(month))
    }

    fun getBudgetItem(budgetId: Int): Flux<InvoiceItem> {
        return repository.getSelectedBudgetItem(budgetId)
            .flatMapMany {
                invoiceService.getInvoiceItemsByCategoryAndMonth(it.categoryId, it.year, it.month)
            }
    }
}
