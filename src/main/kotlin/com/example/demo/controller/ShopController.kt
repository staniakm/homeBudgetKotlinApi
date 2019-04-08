package com.example.demo.controller

import com.example.demo.entity.Shop
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
    fun getShops(): List<Shop> = shopService.getAllShops()

    @GetMapping("/{id}")
    fun getShops(@PathVariable("id") shopId: Long) = shopService.getShopItems(shopId)

    @GetMapping("/{id}/month")
    fun getShopMonthDetails(@PathVariable ("id") shopId: Long) = shopService.getMonthShopDetails(shopId)

    @GetMapping("/{id}/year")
    fun getShopYearDetails(@PathVariable("id") shopId: Long) = shopService.getYearShopDetails(shopId)
}