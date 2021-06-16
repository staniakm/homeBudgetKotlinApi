package com.example.demo.service

import com.example.demo.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(private val repository: CategoryRepository, private val clock: ClockProvider) {

    fun getCategoriesSummary(month: Long) = repository.getCategoriesSummary(clock.getDateFromMonth(month))

    fun getCategoryDetails(id: Long, month: Long) = repository.getCategoryWithDetails(id, clock.getDateFromMonth(month))
}
