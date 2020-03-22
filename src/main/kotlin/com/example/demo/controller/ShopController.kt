package com.example.demo.controller

import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import com.example.demo.service.ShopService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/shop")
class ShopController(private val shopService: ShopService) {

    @GetMapping
    fun getShopsForMonth(@RequestParam("month") month: Long): ResponseEntity<List<ShopSummary>> = ResponseEntity(shopService.getAllShopsForMonth(month), HttpStatus.OK)

    @GetMapping("/{id}")
    fun getShopItems(@PathVariable("id") shopId: Long): ResponseEntity<List<ShopItem>> = ResponseEntity(shopService.getShopItems(shopId), HttpStatus.OK)

    @GetMapping("/{id}/month")
    fun getShopMonthDetails(@PathVariable("id") shopId: Long): ResponseEntity<List<ShopItemsSummary>> = ResponseEntity(shopService.getMonthShopDetails(shopId), HttpStatus.OK)

    @GetMapping("/{id}/year")
    fun getShopYearDetails(@PathVariable("id") shopId: Long): ResponseEntity<List<ShopItemsSummary>> = ResponseEntity(shopService.getYearShopDetails(shopId), HttpStatus.OK)

    @GetMapping("/all")
    fun getAllShops() = ResponseEntity.ok(shopService.findAll())
}
