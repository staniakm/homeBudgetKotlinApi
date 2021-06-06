package com.example.demo.service

import com.example.demo.entity.Category
import com.example.demo.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CategoryService(private val repository: CategoryRepository, private val clock: ClockProvider) {

    fun getCategoriesSummary(month: Long) = repository.getCategoriesSummary(clock.getDateFromMonth(month))

    fun getCategoryDetails(id: Long, month: Long) = repository.getCategoryDetails(id, clock.getDateFromMonth(month))
}
