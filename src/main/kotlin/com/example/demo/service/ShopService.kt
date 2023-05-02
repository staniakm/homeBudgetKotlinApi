package com.example.demo.service

import com.example.demo.entity.*
import com.example.demo.repository.ShopRepository
import org.springframework.stereotype.Service

@Service
class ShopService(private val repository: ShopRepository, private val clock: ClockProvider) {

    fun getShopsSummaryForMonth(month: Long) =
        repository.getAllShopsMonthSummary(clock.getDateFromMonth(month))

    fun getMonthShopItemsSummary(id: Long, month: Long) =
        repository.getShopMonthItems(id, clock.getDateFromMonth(month))

    fun getYearShopItemsSummary(id: Long, month: Long) =
        repository.getShopYearItems(id, clock.getDateFromMonth(month))

    fun getShopItems(shopId: Long) = repository.getShopItems(shopId)
    fun findAllShops() = repository.getAllShops()
    fun createShop(createShopRequest: CreateShopRequest) = repository.createShop(createShopRequest.name.uppercase())
    fun createShopItem(createShopItemRequest: CreateShopItemRequest): ShopItem {
        return repository.createShopItem(createShopItemRequest.shopId, createShopItemRequest.name)
    }
}
