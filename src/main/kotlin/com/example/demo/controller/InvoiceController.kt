package com.example.demo.controller

import com.example.demo.entity.Invoice
import com.example.demo.entity.NewInvoiceRequest
import com.example.demo.entity.UpdateInvoiceAccountRequest
import com.example.demo.service.InvoiceService
import org.springframework.http.ResponseEntity
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
    fun updateInvoiceAccount(@PathVariable invoiceId: Long, @RequestBody update: UpdateInvoiceAccountRequest) =
        invoiceService.updateInvoiceAccount(invoiceId, update)

    @PostMapping("")
    fun createNewInvoice(@RequestBody invoice: NewInvoiceRequest): ResponseEntity<Invoice> {
        return ResponseEntity.ok(invoiceService.createNewInvoiceWithItems(invoice))
    }

    @DeleteMapping("/{invoiceId}")
    fun deleteInvoice(@PathVariable invoiceId: Long) = invoiceService.deleteInvoice(invoiceId)
}
