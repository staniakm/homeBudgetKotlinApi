package com.example.demo.controller

import com.example.demo.entity.ProductDetails
import com.example.demo.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long) = itemService.getProductDetails(id)
}
