package com.example.demo.controller

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.service.InvoiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
class InvoiceController(private val shoppingListService: InvoiceService) {

    @GetMapping
    fun getAllInvoicesForMonth(@RequestParam("month") month: Long): ResponseEntity<List<ShoppingInvoice>> =
            ResponseEntity(shoppingListService.getInvoiceListForMonth(month), HttpStatus.OK)

    @GetMapping("/{id}")
    fun getInvoiceDetails(@PathVariable("id") invoiceId: Long): ResponseEntity<List<ShopCartDetails>> =
            ResponseEntity(shoppingListService.getInvoiceDetails(invoiceId), HttpStatus.OK)
}
