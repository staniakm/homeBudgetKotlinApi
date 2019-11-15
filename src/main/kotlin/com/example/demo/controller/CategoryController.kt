package com.example.demo.controller

import com.example.demo.entity.Category
import com.example.demo.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Objects.isNull

@RestController
@CrossOrigin
@RequestMapping("/api/category")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getCategoriesSummary(@RequestParam("month") month: Long): ResponseEntity<List<Category>> = ResponseEntity(categoryService.getCategoriesSummary(month), HttpStatus.OK)


    @GetMapping("/{id}")
    fun getCategoryDetails(@PathVariable("id") categoryId: Long, @RequestParam("month") month: Long? = 0): ResponseEntity<Category> {
        val monthVal = month ?: 0
        return ResponseEntity(categoryService.getCategoryDetails(categoryId, monthVal), HttpStatus.OK)
    }
}
