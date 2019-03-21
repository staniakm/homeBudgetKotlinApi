package com.example.demo.service

import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService {

    @Autowired
    private lateinit var repository: Repository

    fun getCategories() = repository.getCategoryList()

    fun getCategoryDetails(id: Long) = repository.getCategoryDetails(id)
}
