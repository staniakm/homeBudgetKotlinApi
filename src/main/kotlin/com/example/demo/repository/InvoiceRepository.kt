package com.example.demo.repository

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShopCartDetailsRowMapper
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.entity.ShoppingListRowMapper
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INVOICES
import com.example.demo.repository.SqlQueries.GET_INVOICE
import com.example.demo.repository.SqlQueries.GET_INVOICE_DETAILS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDate


@Service
class InvoiceRepository(private val helper: RepositoryHelper) {

    fun getInvoices(date: LocalDate): Flux<ShoppingInvoice> {
        return helper.getList(GET_INVOICE, ShoppingListRowMapper.map) {
            bind("year", date.year).bind("month", date.monthValue)
        }
    }

    fun getInvoiceDetails(id: Long): Flux<ShopCartDetails> {
        return helper.getList(GET_INVOICE_DETAILS, ShopCartDetailsRowMapper.map) {
            bind("invoiceId", id)
        }
    }

    fun getAccountInvoices(accountId: Long, date: LocalDate): Flux<ShoppingInvoice> {
        return helper.getList(GET_ACCOUNT_INVOICES, ShoppingListRowMapper.map) {
            bind(0, date.year)
            bind(1, date.monthValue)
            bind(2, accountId)
        }
    }
}
