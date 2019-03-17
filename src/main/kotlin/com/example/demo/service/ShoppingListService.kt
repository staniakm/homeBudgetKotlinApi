package com.example.demo.service

import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ShoppingListService {

    @Autowired
    private lateinit var repository: com.example.demo.repository.Repository

    fun getAllLists(): List<ShoppingList> {
        return repository.getInvoices()
    }

//    private fun addLink(item: ShoppingList): ShoppingList {
//        item.add(linkTo(methodOn(com.example.demo.controller.Controller::class.java).getItemDetails(item.listId)).withSelfRel())
//        return item
//    }

    fun getShoppingsDetails(id: Long): List<ShoppingItem> {
        return repository.getInvoiceDetails(id)
    }
}