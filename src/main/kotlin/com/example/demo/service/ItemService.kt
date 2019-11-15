package com.example.demo.service

import com.example.demo.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemService(private val repository: ProductRepository) {

    fun getItemDetails(itemId: Long) = repository.getProductDetails(itemId)

}