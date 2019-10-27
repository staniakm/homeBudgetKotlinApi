package com.example.demo.service

import com.example.demo.entity.Shop
import com.example.demo.repository.ShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShopService {

    @Autowired
    lateinit var repository: ShopRepository

    fun getAllShopsForMonth(month: Long): List<Shop> {
        return LocalDate.now().plusMonths(month).let {
            repository.getAllShops(it)
        }
    }

    fun getMonthShopDetails(id: Long) = repository.getShopMonthItems(id)

    fun getYearShopDetails(id: Long) = repository.getShopYearItems(id)

    fun getShopItems(shopId: Long) = repository.getShopItems(shopId)
}
