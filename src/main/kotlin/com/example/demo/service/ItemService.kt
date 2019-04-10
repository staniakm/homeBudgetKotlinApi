package com.example.demo.service

import com.example.demo.entity.ShopItemsSummary
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemService {

    @Autowired
    lateinit var repository: Repository

//    fun getItem(id: Long): ShopItemsSummary{
//        return repository.getItemById(id)
//    }
}