package com.example.demo.repository

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.jdbc.core.CallableStatementCreator
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameter
import java.sql.CallableStatement
import java.sql.Connection

class RepositoryHelperTest {

    @Test
    fun `should call procedure overload with empty params list`() {
        val jdbcTemplate = mock(JdbcTemplate::class.java)
        val connection = mock(Connection::class.java)
        val callableStatement = mock(CallableStatement::class.java)
        `when`(connection.prepareCall("call test_proc()"))
            .thenReturn(callableStatement)

        doAnswer { invocation ->
            val creator = invocation.getArgument<CallableStatementCreator>(0)
            val params = invocation.getArgument<List<SqlParameter>>(1)
            params.size shouldBe 0
            creator.createCallableStatement(connection)
            emptyMap<String, Any>()
        }.`when`(jdbcTemplate)
            .call(
                ArgumentMatchers.any(CallableStatementCreator::class.java),
                ArgumentMatchers.anyList()
            )

        val helper = RepositoryHelper(jdbcTemplate)
        helper.callProcedureJdbc("call test_proc()") {
            clearWarnings()
        }

        verify(connection).prepareCall("call test_proc()")
        verify(callableStatement).clearWarnings()
    }
}
