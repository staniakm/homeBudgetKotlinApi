package com.example.demo.service

import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.entity.ShopSummary
import com.example.demo.repository.ShopRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShopService(private val repository: ShopRepository, private val clock: ClockProvider) {

    private val getDateFromMonth: (Long) -> LocalDate = { month ->
        clock.getDate()
            .plusMonths(month)
    }

    fun getShopsSummaryForMonth(month: Long): List<ShopSummary> =
        repository.getAllShopsSummary(getDateFromMonth(month))

    fun getMonthShopItemsSummary(id: Long, month: Long): List<ShopItemsSummary> =
        repository.getShopMonthItems(id, getDateFromMonth(month))

    fun getYearShopItemsSummary(id: Long, month: Long): List<ShopItemsSummary> =
        repository.getShopYearItems(id, getDateFromMonth(month))

    fun getShopItems(shopId: Long): List<ShopItem> = repository.getShopItems(shopId)
    fun findAllShops() = repository.getAllShops()
}
