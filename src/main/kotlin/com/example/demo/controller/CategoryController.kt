package com.example.demo.controller

import com.example.demo.entity.Category
import com.example.demo.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/category")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getCategoriesSummary(@RequestParam("month") month: Long): ResponseEntity<List<Category>> {
        return ResponseEntity(categoryService.getCategoriesSummary(month), HttpStatus.OK)
    }


    @GetMapping("/{id}")
    fun getCategoryDetails(@PathVariable("id") categoryId: Long, @RequestParam("month") month: Long? = 0): ResponseEntity<Category> {
        return ResponseEntity(categoryService.getCategoryDetails(categoryId, month ?: 0), HttpStatus.OK)
    }
}
