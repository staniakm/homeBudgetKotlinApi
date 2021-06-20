package com.example.demo.repository

import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import reactor.core.Disposable
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

    fun executeUpdate(query: () -> String, params: DatabaseClient.GenericExecuteSpec.() -> Unit = {}): Mono<Void> {
        return client.sql(query)
            .let {
                it.params()
                it
            }.then()
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

    fun callProcedure(query: String, function: DatabaseClient.GenericExecuteSpec.() -> DatabaseClient.GenericExecuteSpec) {
         client.sql(query)
            .let {
                it.function()
            }.fetch()
            .one()
    }

//    fun callProcedure(query: String, function: Call.() -> Unit) {
//        client.jdbi.withHandle<Any, SQLException> { handle ->
//            handle.createCall("{call dbo.RecalculateBudget (?)}")
//                .apply {
//                    function()
//                }.invoke()
//        }
//    }
}
