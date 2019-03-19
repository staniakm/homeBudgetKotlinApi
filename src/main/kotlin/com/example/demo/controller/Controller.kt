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
import java.util.*


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
    @ResponseBody
    fun getInfo(): List<work> {
        var map= listOf<work>(
                work("TV",1),
                work("work",8),
                work("sleep",8),
                work("learning",4),
                work("other",3)
        )

        return map
    }
}

data class work (val name: String, val value: Int)