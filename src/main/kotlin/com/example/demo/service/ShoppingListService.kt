package com.example.demo.service

import com.example.demo.entity.ChartData
import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShoppingListService {

    @Autowired
    private lateinit var repository: com.example.demo.repository.Repository

    fun getAllLists(monthValue: Long): List<ShoppingList> {
        val date = LocalDate.now().plusMonths(monthValue)
        return repository.getInvoices(date)
    }

//    private fun addLink(item: ShoppingList): ShoppingList {
//        item.add(linkTo(methodOn(com.example.demo.controller.Controller::class.java).getItemDetails(item.listId)).withSelfRel())
//        return item
//    }

    fun getShoppingDetails(id: Long): List<ShoppingItem> {
        return repository.getInvoiceDetails(id)
    }

    fun getMonthChardData(month: Int): List<ChartData> {
        return repository.getMonthSummaryChartData(month)
    }
}