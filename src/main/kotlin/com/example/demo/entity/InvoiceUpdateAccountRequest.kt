package com.example.demo.entity

data class InvoiceUpdateAccountRequest(val oldAccountId: Long, val newAccount: Long, val invoiceId: Long)