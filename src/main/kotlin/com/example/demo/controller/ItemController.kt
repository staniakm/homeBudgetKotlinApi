package com.example.demo.controller

import com.example.demo.service.ItemService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long) = itemService.getProductDetails(id)
}
