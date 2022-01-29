package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INVOICES
import com.example.demo.repository.SqlQueries.GET_INVOICE
import com.example.demo.repository.SqlQueries.GET_INVOICE_DATA
import com.example.demo.repository.SqlQueries.GET_INVOICE_DETAILS
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate


@Service
class InvoiceRepository(private val helper: RepositoryHelper) {

    fun getInvoicesForMonth(date: LocalDate): Flux<ShoppingInvoice> {
        return helper.getList(GET_INVOICE, shoppingListRowMapper) {
            bind("$1", date.year).bind("$2", date.monthValue)
        }
    }

    fun getInvoiceDetails(id: Long): Flux<InvoiceItem> {
        return helper.getList(GET_INVOICE_DETAILS, invoiceItemRowMapper) {
            bind("$1", id)
        }
    }

    fun getInvoice(id: Long): Mono<Invoice> {
        return helper.findOne(GET_INVOICE_DATA, invoiceRowMapper) {
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

    fun updateInvoiceAccount(invoiceId: Long, accountId: Int): Mono<Void> {
        return helper.callProcedure("call changeinvoiceaccount ($1, $2)") {
            bind("$1", invoiceId)
                .bind("$2", accountId)
        }
    }

    fun createInvoice(invoice: NewInvoiceRequest): Mono<Invoice> {
        return helper.executeUpdate(SqlQueries.CREATE_INVOICE) {
            bind("$1", invoice.date)
                .bind("$2", invoice.number)
                .bind("$3", invoice.sum)
                .bind("$4", invoice.description)
                .bind("$5", invoice.accountId)
                .bind("$6", invoice.shopId)
        }.then(getLastInsertedInvoice())
    }

    fun createInvoiceItems(it: Long, items: List<NewInvoiceItemRequest>): Flux<Long> {
        return helper.createInvoiceItems(it, items)
    }

    fun recaculatInvoice(id: Long): Mono<Void> {
        return helper.callProcedure("call recalculateinvoice ($1)") {
            bind("$1", id)
        }

    }

    fun getLastInsertedInvoice(): Mono<Invoice> {
        return helper.findOne(SqlQueries.GET_LAST_INVOICE, invoiceRowMapper)
    }

    fun getInvoiceItemsByCategoryAndDate(categoryId: Int, year: Int, month: Int): Flux<InvoiceItem> {
        return helper.getList(SqlQueries.GET_INVOICE_ITEMS_BY_CATEGORY_AND_DATE, invoiceItemRowMapper) {
            bind("$1", year)
                .bind("$2", month)
                .bind("$3", categoryId)
        }

    }
}
