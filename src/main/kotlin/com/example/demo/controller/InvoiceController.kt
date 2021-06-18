package com.example.demo.controller

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.service.InvoiceService
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux


@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
class InvoiceController(private val shoppingListService: InvoiceService) {

    @GetMapping
    fun getAllInvoicesForMonth(@RequestParam("month") month: Long) = shoppingListService.getInvoiceListForMonth(month)

    @GetMapping("/{id}")
    fun getInvoiceDetails(@PathVariable("id") invoiceId: Long) = shoppingListService.getInvoiceDetails(invoiceId)
}
