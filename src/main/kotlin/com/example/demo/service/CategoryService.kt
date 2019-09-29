package com.example.demo.service

import com.example.demo.entity.Category
import com.example.demo.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CategoryService {

    @Autowired
    private lateinit var repository: CategoryRepository

    fun getCategoriesSummary(month: Long): List<Category> {
        val date = LocalDate.now().plusMonths(month)
        return repository.getCategoriesSummary(date)
    }

    fun getCategoryDetails(id: Long) = repository.getCategoryDetails(id)
}
