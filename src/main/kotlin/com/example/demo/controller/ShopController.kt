package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.ShopService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@CrossOrigin
@RestController
@RequestMapping("/api/shop")
class ShopController(private val shopService: ShopService) {

    @GetMapping
    fun getShopsForMonth(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<Flux<ShopSummary>> {
        return ResponseEntity.ok(shopService.getShopsSummaryForMonth(month).toFlux())
    }


    @GetMapping("/{id}")
    fun getShopItems(@PathVariable("id") shopId: Long): ResponseEntity<Flux<ShopItem>> =
        ResponseEntity(shopService.getShopItems(shopId).toFlux(), HttpStatus.OK)

    @GetMapping("/{id}/month")
    fun getShopMonthDetails(
        @PathVariable("id") shopId: Long,
        @RequestParam("month", defaultValue = "0") month: Long
    ): ResponseEntity<Flux<ShopItemsSummary>> =
        ResponseEntity(shopService.getMonthShopItemsSummary(shopId, month).toFlux(), HttpStatus.OK)

    @GetMapping("/{id}/year")
    fun getShopYearDetails(
        @PathVariable("id") shopId: Long,
        @RequestParam("month", defaultValue = "0") month: Long = 0L
    ): ResponseEntity<Flux<ShopItemsSummary>> =
        ResponseEntity(shopService.getYearShopItemsSummary(shopId, month).toFlux(), HttpStatus.OK)

    @GetMapping("/all")
    fun getAllShops(): ResponseEntity<Flux<Shop>> = ResponseEntity.ok(shopService.findAllShops().toFlux())

    @PostMapping("")
    fun createShop(@RequestBody createShopRequest: CreateShopRequest): Mono<Shop> {
     return   shopService.createShop(createShopRequest).toMono()
    }

    @PostMapping("/newItem")
    fun createNewShopItem(@RequestBody createShopItemRequest: CreateShopItemRequest): ResponseEntity<Mono<ShopItem>> {
        return ResponseEntity.ok(shopService.createShopItem(createShopItemRequest).toMono())
    }
}

