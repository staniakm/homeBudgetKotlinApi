package com.example.demo.controller

import com.example.demo.entity.ProductDetails
import com.example.demo.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long): ResponseEntity<List<ProductDetails>> =
            ResponseEntity(itemService.getProductDetails(id), HttpStatus.OK)
}
