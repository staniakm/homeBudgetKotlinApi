package com.example.demo.controller

import com.example.demo.entity.Assortment
import com.example.demo.service.AssortmentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assortment")
class AssortmentController(private val assortmentService: AssortmentService) {

    @GetMapping("/{id}")
    fun getAssortmentDetails(@PathVariable("id") id: Long): ResponseEntity<AssortmentDetailsResponse> {
        return assortmentService.getAssortmentDetails(id)?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/change-category")
    fun changeAssortmentCategory(@RequestBody changeRequest: AssortmentChangeCategoryRequest): ResponseEntity<AssortmentDetailsResponse> {
        return assortmentService.changeAssortmentCategory(changeRequest.id, changeRequest.categoryId)
            ?.let { ResponseEntity.ok().body(it.toResponse()) }
            ?: ResponseEntity.badRequest().build()
    }

    private fun Assortment.toResponse() = AssortmentDetailsResponse(id, name, categoryId)

}

data class AssortmentChangeCategoryRequest(val id: Long, val categoryId: Long)
data class AssortmentDetailsResponse(val id: Long, val name: String, val categoryId: Long)