package com.example.demo.repository

import com.example.demo.entity.Invoice
import com.example.demo.entity.NewInvoiceItemRequest
import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RepositoryHelper(private val client: DatabaseClient) {
    fun <T> getList(
        query: () -> String,
        mapper: (Row) -> T,
        params: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec
    ): Flux<T> {
        return client.sql(query)
            .let {
                it.params()
            }.map(mapper)
            .all()


    }

    fun <T> getList(
        query: () -> String,
        mapper: (Row) -> T
    ): Flux<T> {
        return client.sql(query)
            .map(mapper)
            .all()
    }

    fun executeUpdate(
        query: () -> String,
        function: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec
    ): Mono<Void> {
        return client.sql(query)
            .let(function)
            .then()
    }

    fun <T> findFirstOrNull(
        query: () -> String,
        mapper: (Row) -> T,
        function: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec
    ): Mono<T> {
        return client.sql(query)
            .let {
                it.function()
            }.map(mapper)
            .one()
    }

    fun <T> findOne(
        query: () -> String,
        mapper: (Row) -> T,
        function: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec
    ): Mono<T> {
        return client.sql(query)
            .let {
                it.function()
            }.map(mapper)
            .one()
    }

    fun <T> findOne(
        query: () -> String,
        mapper: (Row) -> T
    ): Mono<T> {
        return client.sql(query)
            .map(mapper)
            .one()
    }


    fun callProcedure(
        query: String,
        function: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec
    ): Mono<Void> {
        return client.sql(query)
            .let {
                it.function()
            }.then()
    }

    fun callProcedure(
        query: String,
    ): Mono<Void> {
        return client.sql(query)
            .then()
    }

    fun createInvoiceItems(invoiceId: Long, items: List<NewInvoiceItemRequest>): Flux<Long> {
        return client.inConnectionMany { con ->
            var statement = con.createStatement(SqlQueries.CREATE_INVOICE_DETAILS.invoke()).returnGeneratedValues("id")

            for (item in items) {
                statement.bind(0, invoiceId)
                    .bind(1, item.totalPrice)
                    .bind(2, item.amount)
                    .bind(3, item.unitPrice)
                    .bind(4, item.discount)
                    .bind(5, item.shopItem.itemId)
                    .add()
            }
            Flux.from(statement.execute())
                .flatMap {
                    it.map { row, _ -> row["id"] as Long }
                }
        }
    }
}
