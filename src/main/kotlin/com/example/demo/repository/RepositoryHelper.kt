package com.example.demo.repository

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Call
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.core.statement.Update
import org.springframework.stereotype.Service
import java.sql.SQLException
import java.util.*

@Service
class RepositoryHelper(private val jdbi: Jdbi) {
    fun <T> getList(query: () -> String, mapper: RowMapper<T>, params: Query.() -> Unit = {}): List<T> {
        return jdbi.withHandle<List<T>, SQLException> { handle ->
            handle.createQuery(query())
                .apply {
                    params()
                }.map(mapper)
                .list()
        }
    }

    fun executeUpdate(query: () -> String, params: Update.() -> Unit = {}) {
        jdbi.withHandle<Any, SQLException> { handle ->
            handle.createUpdate(query()).apply {
                params()
            }.execute()
        }
    }

    fun <T> findFirstOrNull(query: () -> String, mapper: RowMapper<T>, function: Query.() -> Unit): T? {
        return jdbi.withHandle<T, SQLException> { handle ->
            handle.createQuery(query())
                .apply {
                    function()
                }.map(mapper)
                .firstOrNull()
        }
    }

    fun <T> findOne(query: () -> String, mapper: RowMapper<T>, function: Query.() -> Unit): Optional<T> {
        return jdbi.withHandle<Optional<T>, SQLException> { handle ->
            handle.createQuery(query())
                .apply {
                    function()
                }.map(mapper)
                .findOne()
        }
    }

    fun callProcedure(query: String, function: Call.() -> Unit) {
        jdbi.withHandle<Any, SQLException> { handle ->
            handle.createCall("{call dbo.RecalculateBudget (?)}")
                .apply {
                    function()
                }.invoke()
        }
    }
}
