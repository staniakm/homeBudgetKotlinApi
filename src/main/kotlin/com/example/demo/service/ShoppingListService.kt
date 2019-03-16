package com.example.demo.service

import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShoppingListService{

    @Autowired
   private lateinit var repository: com.example.demo.repository.Repository

    fun getAllLists():List<ShoppingList> {
       return repository.getInvoices()
    }

    fun getShoppingsDetails(id: Long): List<ShoppingItem>?{
        return repository.getInvoiceDetails(id)
    }
}