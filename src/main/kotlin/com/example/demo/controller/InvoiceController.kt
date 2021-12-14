package com.example.demo.controller

import com.example.demo.service.InvoiceService
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
class InvoiceController(private val shoppingListService: InvoiceService) {

    @GetMapping
    fun getAllInvoicesForMonth(@RequestParam("month", defaultValue = "0") month: Long) =
        shoppingListService.getInvoiceListForMonth(month)

    @GetMapping("/{id}")
    fun getInvoiceDetails(@PathVariable("id") invoiceId: Long) = shoppingListService.getInvoiceDetails(invoiceId)
}
