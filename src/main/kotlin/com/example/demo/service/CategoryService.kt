package com.example.demo.service

import com.example.demo.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CategoryService {

    @Autowired
    private lateinit var repository: CategoryRepository

    fun getCategories(month: Long): List<Any> {
        val date = LocalDate.now().plusMonths(month)
        return repository.getCategoryList(date)
    }

    fun getCategoryDetails(id: Long) = repository.getCategoryDetails(id)
}
