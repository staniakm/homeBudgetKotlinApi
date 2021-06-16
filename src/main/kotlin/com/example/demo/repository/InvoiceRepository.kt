package com.example.demo.repository

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShopCartDetailsRowMapper
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.entity.ShoppingListRowMapper
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INVOICES
import com.example.demo.repository.SqlQueries.GET_INVOICE
import com.example.demo.repository.SqlQueries.GET_INVOICE_DETAILS
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class InvoiceRepository(private val helper: RepositoryHelper) {

    fun getInvoices(date: LocalDate): List<ShoppingInvoice> {
        return helper.getList(GET_INVOICE, ShoppingListRowMapper()) {
            bind(0, date.year)
            bind(1, date.monthValue)
        }
    }

    fun getInvoiceDetails(id: Long): List<ShopCartDetails> {
        return helper.getList(GET_INVOICE_DETAILS, ShopCartDetailsRowMapper()) {
            bind(0, id)
        }
    }

    fun getAccountInvoices(accountId: Long, date: LocalDate): List<ShoppingInvoice> {
        return helper.getList(GET_ACCOUNT_INVOICES, ShoppingListRowMapper()) {
            bind(0, date.year)
            bind(1, date.monthValue)
            bind(2, accountId)
        }
    }
}
