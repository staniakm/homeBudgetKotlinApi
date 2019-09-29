package com.example.demo.controller

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
    fun getCategoriesSummary(@RequestParam("month") month: Long) = categoryService.getCategoriesSummary(month)


    @GetMapping("/{id}")
    fun getCategoryDetails(@PathVariable("id") categoryId: Long) = categoryService.getCategoryDetails(categoryId)
}