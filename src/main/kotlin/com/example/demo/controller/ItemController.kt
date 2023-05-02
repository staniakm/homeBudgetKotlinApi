package com.example.demo.controller

import com.example.demo.service.ItemService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long) = itemService.getProductDetails(id)

    @PutMapping("/{id}/update-category")
    fun updateCategory(@PathVariable("id") id: Long, @RequestParam("categoryId") categoryId: Long) =
        itemService.updateCategory(id, categoryId)
}
