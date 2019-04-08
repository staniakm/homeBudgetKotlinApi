package com.example.demo.service

import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShopService {

    @Autowired
    lateinit var repository: Repository

    fun getAllShops() = repository.getAllShops()

    fun getMonthShopDetails(id: Long) = repository.getShopMonthItems(id)

    fun getYearShopDetails(id: Long) = repository.getShopYearItems(id)

    fun getShopItems(shopId: Long) = repository.getShopItems(shopId)
}
