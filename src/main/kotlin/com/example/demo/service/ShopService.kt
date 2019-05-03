package com.example.demo.service

import com.example.demo.entity.Shop
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShopService {

    @Autowired
    lateinit var repository: Repository

    fun getAllShops(month: Long):List<Shop> {
        val date = LocalDate.now().plusMonths(month)
        return repository.getAllShops(date)
    }

    fun getMonthShopDetails(id: Long) = repository.getShopMonthItems(id)

    fun getYearShopDetails(id: Long) = repository.getShopYearItems(id)

    fun getShopItems(shopId: Long) = repository.getShopItems(shopId)
}
