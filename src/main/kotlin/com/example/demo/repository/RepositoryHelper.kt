package com.example.demo.repository

import com.example.demo.entity.NewInvoiceItemRequest
import com.example.demo.repository.SqlQueries.CREATE_INVOICE_DETAILS
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@Service
class RepositoryHelper(val jdbcTemplate: JdbcTemplate) {
    fun batchCreateInvoiceItems(invoiceId: Long, items: List<NewInvoiceItemRequest>) {
        jdbcTemplate.batchUpdate(CREATE_INVOICE_DETAILS.invoke(),
            items
                .map { item ->
                    arrayOf(
                        invoiceId,
                        item.totalPrice,
                        item.amount,
                        item.unitPrice,
                        item.discount,
                        item.shopItem.itemId
                    )
                }
        )
    }

    fun <T> jdbcQueryGetFirst(
        query: () -> String,
        params: PreparedStatement.() -> Unit,
        mapper: (ResultSet, Any?) -> T
    ): T? {
        return jdbcQueryGetList(query, params, mapper).firstOrNull()
    }

    fun <T> jdbcQueryGetList(
        query: () -> String,
        params: PreparedStatement.() -> Unit,
        mapper: (ResultSet, Any?) -> T
    ): List<T> {
        return jdbcTemplate.query({
            with(it.prepareStatement(query.invoke())) {
                params.invoke(this)
                this
            }
        }, mapper)
    }

    fun updateJdbc(
        query: () -> String,
        params: PreparedStatement.() -> Unit
    ): Int {
        return jdbcTemplate.update({
            with(it.prepareStatement(query.invoke())) {
                params.invoke(this)
                this
            }
        })
    }

    fun insertWithReturnKeyJdbc(
        query: () -> String,
        params: PreparedStatement.() -> Unit
    ): Long? {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({
            with(it.prepareStatement(query.invoke(), Statement.RETURN_GENERATED_KEYS)) {
                params.invoke(this)
                this
            }
        }, keyHolder)
        val key = keyHolder.key
        println("key: $key")
        return key?.toLong()
    }

    fun callProcedureJdbc(query: String, function: CallableStatement.() -> Unit) {
        jdbcTemplate.call(
            {
                it.prepareCall(query).apply(function)
            }, listOf(
                SqlParameter("product", java.sql.Types.VARCHAR),
                SqlParameter("shopid", java.sql.Types.INTEGER)
            )
        )
    }
}
