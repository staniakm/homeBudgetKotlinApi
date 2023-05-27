package com.example.demo.service

import com.example.demo.entity.Category
import com.example.demo.entity.CategorySummary
import com.example.demo.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CategoryService(private val repository: CategoryRepository, private val clock: ClockProviderInterface) {

    fun getCategoriesSummary(month: Long, skipZero: Boolean): List<CategorySummary> {
        return repository.getCategoriesSummary(clock.getDateFromMonth(month)).let { categories ->
            if (skipZero) {
                categories.filter { it.monthSummary != BigDecimal("0.00") }
            } else {
                categories
            }
        }
    }

    fun getCategory(id: Long, month: Long) = repository.getCategory(id, clock.getDateFromMonth(month))
    fun getCategoryDetails(id: Long, month: Long) =
        repository.getProductsForCategoryAndMonth(id, clock.getDateFromMonth(month))

    fun getCategoryById(categoryId: Long): Category? {
        return repository.getCategoryById(categoryId)
    }

}
