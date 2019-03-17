package com.example.demo.controller

import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import com.example.demo.service.ShoppingListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/shopping")
class Controller{

    @Autowired
    lateinit var shoppingListService : ShoppingListService

    @GetMapping()
    fun getAllLists(): ResponseEntity<List<ShoppingList>> {
        val list = shoppingListService.getAllLists()
        return ResponseEntity(list, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long):List<ShoppingItem>{
        return shoppingListService.getShoppingsDetails(id)
    }

    @GetMapping("/info")
    fun getInfo():String{
        return "hello world"
    }

}