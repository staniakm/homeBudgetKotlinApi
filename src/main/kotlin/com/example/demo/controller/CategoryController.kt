package com.example.demo.controller

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/category")
class CategoryController {

    @Autowired
    private lateinit var categoryService: CategoryService

    @GetMapping
    fun getCategories (): List<Category> {
        return categoryService.getCategories()
    }

    @GetMapping("/{id}")
    fun getCategoryDetails(@PathVariable id : Long): List<CategoryDetails>{
        return categoryService.getCategoryDetails(id)
    }
}