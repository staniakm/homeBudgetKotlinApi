package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.ShopService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@CrossOrigin
@RestController
@RequestMapping("/api/shop")
class ShopController(private val shopService: ShopService) {

    @GetMapping
    fun getShopsForMonth(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<Flux<ShopSummary>> =
        ResponseEntity(shopService.getShopsSummaryForMonth(month), HttpStatus.OK)

    @GetMapping("/{id}")
    fun getShopItems(@PathVariable("id") shopId: Long): ResponseEntity<Flux<ShopItem>> =
        ResponseEntity(shopService.getShopItems(shopId), HttpStatus.OK)

    @GetMapping("/{id}/month")
    fun getShopMonthDetails(
        @PathVariable("id") shopId: Long,
        @RequestParam("month", defaultValue = "0") month: Long
    ): ResponseEntity<Flux<ShopItemsSummary>> =
        ResponseEntity(shopService.getMonthShopItemsSummary(shopId, month), HttpStatus.OK)

    @GetMapping("/{id}/year")
    fun getShopYearDetails(
        @PathVariable("id") shopId: Long,
        @RequestParam("month", defaultValue = "0") month: Long = 0L
    ): ResponseEntity<Flux<ShopItemsSummary>> =
        ResponseEntity(shopService.getYearShopItemsSummary(shopId, month), HttpStatus.OK)

    @GetMapping("/all")
    fun getAllShops(): ResponseEntity<Flux<Shop>> = ResponseEntity.ok(shopService.findAllShops())

    @PostMapping("")
    fun createShop(@RequestBody createShopRequest: CreateShopRequest) = ResponseEntity.ok(shopService.createShop(createShopRequest))
}
