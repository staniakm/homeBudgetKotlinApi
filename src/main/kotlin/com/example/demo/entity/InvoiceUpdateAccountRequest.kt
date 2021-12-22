package com.example.demo.entity

data class InvoiceUpdateAccountRequest(val oldAccountId: Int, val newAccount: Int, val invoiceId: Long)