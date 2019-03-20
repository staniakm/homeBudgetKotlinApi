package com.example.demo.controller

import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import com.example.demo.service.ShoppingListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/shopping")
class Controller{

    @Autowired
    lateinit var shoppingListService : ShoppingListService

    @GetMapping
    fun getAllLists(@RequestParam("month") month:Long): ResponseEntity<List<ShoppingList>> {
        val list = shoppingListService.getAllLists(month)
        return ResponseEntity(list, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getItemDetails(@PathVariable("id") id: Long):List<ShoppingItem>{
        return shoppingListService.getShoppingDetails(id)
    }

    @GetMapping("/info")
    @ResponseBody
    fun getInfo(): String {
        return "Info endpoint"
    }
}
