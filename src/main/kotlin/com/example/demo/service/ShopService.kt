package com.example.demo.service

import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import com.example.demo.repository.ShopRepository
import org.springframework.stereotype.Service

@Service
class ShopService(private val repository: ShopRepository, private val clock: ClockProvider) {

    fun getShopsSummaryForMonth(month: Long): List<ShopSummary> =
        repository.getAllShopsSummary(clock.getDateFromMonth(month))

    fun getMonthShopItemsSummary(id: Long, month: Long): List<ShopItemsSummary> =
        repository.getShopMonthItems(id, clock.getDateFromMonth(month))

    fun getYearShopItemsSummary(id: Long, month: Long): List<ShopItemsSummary> =
        repository.getShopYearItems(id, clock.getDateFromMonth(month))

    fun getShopItems(shopId: Long): List<ShopItem> = repository.getShopItems(shopId)
    fun findAllShops() = repository.getAllShops()
}
