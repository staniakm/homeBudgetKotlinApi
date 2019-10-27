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
        return LocalDate.now().plusMonths(month)
                .let {
                    repository.getCategoriesSummary(it)
                }
    }

    fun getCategoryDetails(id: Long) = repository.getCategoryDetails(id)
}
