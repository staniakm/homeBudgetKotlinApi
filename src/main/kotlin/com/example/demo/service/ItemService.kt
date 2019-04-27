package com.example.demo.service

import com.example.demo.entity.ShopItemsSummary
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Service
class ItemService {

    @Autowired
    lateinit var repository: Repository

    fun getItemDetails(itemId: Long) = repository.getProductDetails(itemId)

}