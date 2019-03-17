package com.example.demo.controller

import com.example.demo.entity.Shop
import com.example.demo.service.ShopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}