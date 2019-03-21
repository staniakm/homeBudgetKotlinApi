package com.example.demo.controller

import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController {

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long) = "Item details"
}