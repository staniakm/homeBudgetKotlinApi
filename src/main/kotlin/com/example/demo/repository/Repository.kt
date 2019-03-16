package com.example.demo.repository

import com.example.demo.entity.ShoppingItem
import com.example.demo.entity.ShoppingList
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE_DETAILS
import com.example.demo.repository.SqlQueries.getQuerry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.sql.DriverManager
import java.sql.ResultSet


@Service
class Repository {

    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getInvoices(): List<ShoppingList> {
        val list = ArrayList<ShoppingList>()
        println("Connecting...")
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        val sql = getQuerry(GET_INVOICE)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.createStatement().use { statement ->
                statement.executeQuery(sql).use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ShoppingList(
                                resultSet.getLong("id"),
                                resultSet.getString("sklep"),
                                resultSet.getDate("data"),
                                resultSet.getBigDecimal("suma")
                        ))
                    }
                }
            }
        }
        return list
    }

    fun getInvoiceDetails(id : Long):List<ShoppingItem>{
        val items = ArrayList<ShoppingItem>()
        val sql = getQuerry(GET_INVOICE_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1,id)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        items.add(ShoppingItem(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getDouble("ilosc"),
                                resultSet.getDouble("cena"),
                                resultSet.getDouble("cena_za_jednostke"),
                                resultSet.getString("opis")
                        ))
                    }
                }
            }
        }
        return items

    }
}