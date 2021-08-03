package com.example.demo.repository

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.entity.shopCartDetailsRowMapper
import com.example.demo.entity.shoppingListRowMapper
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INVOICES
import com.example.demo.repository.SqlQueries.GET_INVOICE
import com.example.demo.repository.SqlQueries.GET_INVOICE_DETAILS
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDate


@Service
class InvoiceRepository(private val helper: RepositoryHelper) {

    fun getInvoices(date: LocalDate): Flux<ShoppingInvoice> {
        println("fetch data $date")
        return helper.getList(GET_INVOICE, shoppingListRowMapper) {
            bind("year", date.year).bind("month", date.monthValue)
        }
    }

    fun getInvoiceDetails(id: Long): Flux<ShopCartDetails> {
        return helper.getList(GET_INVOICE_DETAILS, shopCartDetailsRowMapper) {
            bind("invoiceId", id)
        }
    }

    fun getAccountInvoices(accountId: Long, date: LocalDate): Flux<ShoppingInvoice> {
        return helper.getList(GET_ACCOUNT_INVOICES, shoppingListRowMapper) {
            bind(0, date.year)
            bind(1, date.monthValue)
            bind(2, accountId)
        }
    }
}
