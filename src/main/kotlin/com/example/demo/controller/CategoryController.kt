package com.example.demo.controller

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/category")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getCategoriesSummary(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<List<Category>> {
        return ResponseEntity(categoryService.getCategoriesSummary(month), HttpStatus.OK)
    }


    @GetMapping("/{id}")
    fun getCategory(
        @PathVariable("id") categoryId: Long,
        @RequestParam("month", defaultValue = "0") month: Long
    ): ResponseEntity<Category> {
        return ResponseEntity(categoryService.getCategory(categoryId, month), HttpStatus.OK)
    }

    @GetMapping("/{id}/details")
    fun getCategoryDetails(
        @PathVariable("id") categoryId: Long,
        @RequestParam("month", defaultValue = "0") month: Long = 0
    ): ResponseEntity<List<CategoryDetails>> {
        return ResponseEntity(categoryService.getCategoryDetails(categoryId, month), HttpStatus.OK)
    }
}
