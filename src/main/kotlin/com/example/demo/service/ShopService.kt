package com.example.demo.service

import com.example.demo.entity.Shop
import com.example.demo.entity.ShopSummary
import com.example.demo.repository.ShopRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShopService(private val repository: ShopRepository) {

    fun getAllShopsForMonth(month: Long): List<ShopSummary> {
        return LocalDate.now().plusMonths(month).let {
            repository.getAllShopsSummary(it)
        }
    }

    fun getMonthShopDetails(id: Long) = repository.getShopMonthItems(id)

    fun getYearShopDetails(id: Long) = repository.getShopYearItems(id)

    fun getShopItems(shopId: Long) = repository.getShopItems(shopId)
    fun findAll(): List<Shop> {
        return repository.getAllShops()
    }
}
