package com.example.demo.controller

import com.example.demo.entity.InvoiceUpdateAccountRequest
import com.example.demo.service.InvoiceService
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
class InvoiceController(private val invoiceService: InvoiceService) {

    @GetMapping
    fun getAllInvoicesForMonth(@RequestParam("month", defaultValue = "0") month: Long) =
        invoiceService.getInvoiceListForMonth(month)

    @GetMapping("/{id}")
    fun getInvoiceDetails(@PathVariable("id") invoiceId: Long) = invoiceService.getInvoiceDetails(invoiceId)

    @PutMapping("/{invoiceId}")
    fun updateInvoiceAccount(@PathVariable invoiceId: Long, @RequestBody update: InvoiceUpdateAccountRequest) = invoiceService.updateInvoiceAccount(invoiceId, update)
}
