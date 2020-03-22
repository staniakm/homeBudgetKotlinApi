package com.example.demo.repository

import com.example.demo.entity.ShopCartDetails
import com.example.demo.entity.ShoppingList
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE_DETAILS
import com.example.demo.repository.SqlQueries.getQuery
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.DriverManager
import java.time.LocalDate


@Service
class InvoiceRepository {

    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getInvoices(date: LocalDate): List<ShoppingList> {
        val list = ArrayList<ShoppingList>()
        val sql = getQuery(GET_INVOICE)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ShoppingList(
                                resultSet.getLong("id"),
                                resultSet.getString("sklep"),
                                resultSet.getDate("data"),
                                resultSet.getBigDecimal("suma"),
                                resultSet.getString("account")
                        ))
                    }
                }
            }
        }
        return list
    }

    fun getInvoiceDetails(id: Long): List<ShopCartDetails> {
        val sql = getQuery(GET_INVOICE_DETAILS)
        val items = ArrayList<ShopCartDetails>()
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1, id)
                statement.executeQuery().use { rs ->
                    while (rs.next()) {
                        items.add(ShopCartDetails(
                                rs.getLong("id"),
                                rs.getString("nazwa"),
                                rs.getBigDecimal("ilosc"),
                                rs.getBigDecimal("cena_za_jednostke"),
                                rs.getBigDecimal("rabat"),
                                rs.getBigDecimal("cena"),
                                rs.getLong("itemId")
                        ))

                    }
                }
            }
        }
        return items
    }


}
