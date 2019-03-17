package com.example.demo.service

import com.example.demo.entity.Shop
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShopService {

    @Autowired
    lateinit var repository: Repository

    fun getAllShops(): List<Shop> {
        return repository.getAllShops()
    }

}
