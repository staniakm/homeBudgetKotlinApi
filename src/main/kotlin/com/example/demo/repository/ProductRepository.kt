package com.example.demo.repository

import com.example.demo.entity.ProductDetails
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager

@Repository
class ProductRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getProductDetails(productId: Long): List<ProductDetails> {
        val list = ArrayList<ProductDetails>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_PRODUCT_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1, productId)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ProductDetails(
                                resultSet.getString("sklep"),
                                resultSet.getDate("data"),
                                resultSet.getBigDecimal("cena"),
                                resultSet.getDouble("ilosc"),
                                resultSet.getBigDecimal("rabat"),
                                resultSet.getBigDecimal("suma"),
                                resultSet.getLong("invoiceId"),
                                resultSet.getLong("invoiceItemId")
                        ))
                    }
                }
            }
        }
        return list
    }
}