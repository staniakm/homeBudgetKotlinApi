package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.CREATE_INVOICE
import com.example.demo.repository.SqlQueries.GET_ACCOUNT_INVOICES
import com.example.demo.repository.SqlQueries.GET_INVOICE
import com.example.demo.repository.SqlQueries.GET_INVOICE_DATA
import com.example.demo.repository.SqlQueries.GET_INVOICE_DETAILS
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Date
import java.time.LocalDate


@Service
class InvoiceRepository(private val helper: RepositoryHelper) {
    val logger = LoggerFactory.getLogger(this.javaClass)
    fun getInvoicesForMonth(date: LocalDate): List<ShoppingInvoice> {
        return helper.jdbcQueryGetList(GET_INVOICE, {
            setInt(1, date.year)
            setInt(2, date.monthValue)
        }, shoppingListRowMapper)
    }

    fun getInvoiceDetails(id: Long): List<InvoiceItem> {
        return helper.jdbcQueryGetList(GET_INVOICE_DETAILS, {
            setInt(1, id.toInt())
        }, invoiceItemRowMapper)
    }

    fun getInvoice(id: Long): Invoice? {
        logger.info("Get invoice")
        return helper.jdbcQueryGetFirst(GET_INVOICE_DATA, {
            setInt(1, id.toInt())
        }, invoiceRowMapper)
    }

    fun getInvoiceJdbc(id: Long): Invoice {
        logger.info("Get invoice")
        return helper.jdbcQueryGetFirst(GET_INVOICE_DATA, {
            setInt(1, id.toInt())
        }, invoiceRowMapper)!!
    }

    fun getAccountInvoices(accountId: Long, date: LocalDate): List<ShoppingInvoice> {
        return helper.jdbcQueryGetList(GET_ACCOUNT_INVOICES, {
            setInt(1, date.year)
            setInt(2, date.monthValue)
            setInt(3, accountId.toInt())
        }, shoppingListRowMapper)
    }

    fun updateInvoiceAccount(invoiceId: Long, accountId: Int): Unit {
        return helper.callProcedureJdbc("call changeinvoiceaccount (?, ?)") {
            setInt(1, invoiceId.toInt())
            setInt(2, accountId)
        }
    }

    fun createInvoice(invoice: NewInvoiceRequest): Invoice? {
        return helper.insertWithReturnKeyJdbc(CREATE_INVOICE) {
            setDate(1, Date.valueOf(invoice.date))
            setString(2, invoice.number)
            setBigDecimal(3, invoice.sum)
            setString(4, invoice.description)
            setInt(5, invoice.accountId)
            setInt(6, invoice.shopId)
        }?.let {
            helper.jdbcQueryGetFirst(GET_INVOICE_DATA, {
                setInt(1, it.toInt())
            }, invoiceRowMapper)
        }

    }

    fun createInvoiceItems(invoiceId: Long, items: List<NewInvoiceItemRequest>): Unit {
        helper.batchCreateInvoiceItems(invoiceId, items)
    }

    fun recaculatInvoice(id: Long): Unit {
        helper.callProcedureJdbc("call recalculateinvoice (?)") {
            setLong(1, id)
        }
    }

    fun getInvoiceItemsByCategoryAndDate(categoryId: Int, year: Int, month: Int): List<InvoiceItem> {
        return helper.jdbcQueryGetList(SqlQueries.GET_INVOICE_ITEMS_BY_CATEGORY_AND_DATE, {
            setInt(1, year)
            setInt(2, month)
            setInt(3, categoryId)
        }, invoiceItemRowMapper)

    }

    fun createAutoInvoice(): Unit {
        return helper.callProcedureJdbc("call autoinvoice ()") {
        }
    }

    fun deleteDetails(invoiceId: Long): Long {
        logger.info("Delete invoice details")
        return helper.updateJdbc(SqlQueries.DELETE_INVOICE_DETAILS) {
            setLong(1, invoiceId)
        }.let {
            invoiceId
        }

    }

    fun deleteInvoice(invoiceId: Long): Long {
        logger.info("Delete invoice")
        return helper
            .updateJdbc(SqlQueries.DELETE_INVOICE) {
                setLong(1, invoiceId)
            }.let {
                invoiceId
            }
    }
}
