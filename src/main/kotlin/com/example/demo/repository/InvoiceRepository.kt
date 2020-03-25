package com.example.demo.repository

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShopCartDetailsRowMapper
import com.example.demo.entity.ShoppingList
import com.example.demo.entity.ShoppingListRowMapper
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE_DETAILS
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Service
import java.sql.SQLException
import java.time.LocalDate


@Service
class InvoiceRepository(private val jdbi: Jdbi) {

    fun getInvoices(date: LocalDate): List<ShoppingList> {

        return jdbi.withHandle<List<ShoppingList>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_INVOICE))
                    .bind(0, date.year)
                    .bind(1, date.monthValue)
                    .map(ShoppingListRowMapper())
                    .list()
        }
    }

    fun getInvoiceDetails(id: Long): List<ShopCartDetails> {

        return jdbi.withHandle<List<ShopCartDetails>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_INVOICE_DETAILS))
                    .bind(0, id)
                    .map(ShopCartDetailsRowMapper())
                    .list()
        }
    }
}
