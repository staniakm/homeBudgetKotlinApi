package com.example.demo.service

import com.example.demo.entity.ShoppingItem
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemService {

    @Autowired
    lateinit var repository: Repository

    fun getItem(id: Long): ShoppingItem{
        return repository.getItemById(id)
    }
}