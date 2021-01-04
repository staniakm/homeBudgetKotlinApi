package com.example.demo

import com.example.demo.entity.Account
import com.example.demo.entity.AccountRowMapper
import com.example.demo.repository.SqlQueries
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.sql.SQLException

@SpringBootTest
class ShoppingInvoiceApplicationTests() {

    @Autowired
    private lateinit var jdbiTemplate: Jdbi

    @Test
    fun contextLoads() {
    }

    @Test
    fun select() {
        val shops = jdbiTemplate.withHandle<List<Account>, SQLException> { handle ->
            handle.createQuery(SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_ACCOUNT_DATA))
                    .map(AccountRowMapper())
                    .list()
        }

        println(shops)

    }

}
