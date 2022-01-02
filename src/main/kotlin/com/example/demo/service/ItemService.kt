package com.example.demo.service

import com.example.demo.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ItemService(private val repository: ProductRepository) {

    fun getProductDetails(itemId: Long)= repository.getProductHistory(itemId)
}
