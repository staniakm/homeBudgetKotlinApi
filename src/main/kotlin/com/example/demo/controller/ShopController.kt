package com.example.demo.controller

import com.example.demo.entity.Shop
import com.example.demo.entity.ShoppingItem
import com.example.demo.service.ShopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/shop")
class ShopController {

    @Autowired
    lateinit var shopService: ShopService

    @GetMapping
    fun getShops(): List<Shop>{
        return shopService.getAllShops()
    }

    @GetMapping("/{id}/month")
    fun getShopMonthDetails(@PathVariable id: Long):List<ShoppingItem>{
        return shopService.getMonthShoppings(id)
    }
}