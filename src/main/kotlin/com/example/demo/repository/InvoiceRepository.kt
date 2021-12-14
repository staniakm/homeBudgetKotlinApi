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
            bind("$1", date.year).bind("$2", date.monthValue)
        }
    }

    fun getInvoiceDetails(id: Long): Flux<ShopCartDetails> {
        return helper.getList(GET_INVOICE_DETAILS, shopCartDetailsRowMapper) {
            bind("$1", id)
        }
    }

    fun getAccountInvoices(accountId: Long, date: LocalDate): Flux<ShoppingInvoice> {
        return helper.getList(GET_ACCOUNT_INVOICES, shoppingListRowMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
                .bind("$3", accountId)
        }
    }
}
