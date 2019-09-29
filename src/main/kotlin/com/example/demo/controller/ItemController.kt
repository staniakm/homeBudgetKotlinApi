package com.example.demo.controller

import com.example.demo.service.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/item")
@RestController
class ItemController {

    @Autowired
    lateinit var itemService: ItemService

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long) =
            ResponseEntity(itemService.getItemDetails(id), HttpStatus.OK)
}