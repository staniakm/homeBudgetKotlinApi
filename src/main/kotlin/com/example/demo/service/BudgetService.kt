package com.example.demo.service

import com.example.demo.entity.*
import com.example.demo.repository.BudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.time.LocalDate

@Service
class BudgetService(
    private val repository: BudgetRepository,
    private val invoiceService: InvoiceService,
    private val clock: ClockProvider
) {

    fun getMonthBudget(month: Long) = repository.getBudgetForMonth(clock.getDateFromMonth(month))

    @Transactional
    fun updateBudget(updateBudget: UpdateBudgetDto): MonthBudgetPlanned? {
        repository.updateBudget(updateBudget)
        return repository.getSelectedBudgetItem(updateBudget.budgetId)
    }

    fun recalculateBudgets(month: Long): MonthBudget {
        repository.recalculateBudgets(clock.getDateFromMonth(month))
        return getMonthBudget(month)
    }

    fun getBudgetItem(budgetId: Int): List<InvoiceItem> {
        return repository.getSelectedBudgetItem(budgetId)
            ?.let {
                invoiceService.getInvoiceItemsByCategoryAndMonth(it.categoryId, it.year, it.month)
            } ?: listOf()
    }
}
