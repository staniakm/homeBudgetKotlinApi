package com.example.demo.controller

import com.example.demo.service.InvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
class InvoiceController {

    @Autowired
    lateinit var shoppingListService: InvoiceService

    @GetMapping
    fun getAllInvoicesForMonth(@RequestParam("month") month: Long) =
            ResponseEntity(shoppingListService.getInvoiceListForMonth(month), HttpStatus.OK)

    @GetMapping("/{id}")
    fun getInvoiceDetails(@PathVariable("id") invoiceId: Long) =
            ResponseEntity(shoppingListService.getInvoiceDetails(invoiceId), HttpStatus.OK)

    @GetMapping("/info")
    fun getInfo() = "Info endpoint"
}
