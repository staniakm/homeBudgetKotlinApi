package com.example.demo.service

import com.example.demo.entity.Shop
import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import com.example.demo.repository.ShopRepository
import org.springframework.stereotype.Service

@Service
class ShopService(private val repository: ShopRepository, private val clock: ClockProvider) {

    fun getShopsSummaryForMonth(month: Long): List<ShopSummary> {
        return clock.getDate()
            .plusMonths(month)
            .let {
                repository.getAllShopsSummary(it)
            }
    }

    fun getMonthShopItemsSummary(id: Long, month: Long): List<ShopItemsSummary> =
        clock.getDate()
            .plusMonths(month)
            .let {
                repository.getShopMonthItems(id, it)
            }

    fun getYearShopItemsSummary(id: Long, month:Long): List<ShopItemsSummary> =
        clock.getDate()
            .plusMonths(month)
            .let {
                repository.getShopYearItems(id, it)
            }


    fun getShopItems(shopId: Long): List<ShopItem> = repository.getShopItems(shopId)
    fun findAllShops(): List<Shop> {
        return repository.getAllShops()
    }
}
