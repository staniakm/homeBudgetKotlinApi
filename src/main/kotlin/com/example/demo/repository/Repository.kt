package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.QUERY_TYPE.*
import com.example.demo.repository.SqlQueries.getQuery
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.DriverManager
import java.sql.ResultSet
import java.time.LocalDate


@Service
class Repository {

    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getInvoices(date: LocalDate): List<ShoppingList> {
        val list = ArrayList<ShoppingList>()
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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
                                resultSet.getBigDecimal("suma")
                        ))
                    }
                }
            }
        }
        return list
    }

    fun getInvoiceDetails(id: Long): List<ShoppingItem> {
        val sql = getQuery(GET_INVOICE_DETAILS)
        return getShoppingItems(sql, id)
    }

    private fun mapToShoppingItem(items: ArrayList<ShoppingItem>, resultSet: ResultSet) {
        items.add(ShoppingItem(
                resultSet.getLong("id"),
                resultSet.getString("nazwa"),
                resultSet.getBigDecimal("ilosc"),
                resultSet.getBigDecimal("cena"),
                resultSet.getBigDecimal("cena_za_jednostke"),
                resultSet.getString("opis")
        ))
    }

    fun getCategoryList(): List<Category> {
        val items = ArrayList<Category>()
        val sql = getQuery(GET_CATEGORY_LIST)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.createStatement().use { statement ->
                statement.executeQuery(sql).use { resultSet ->
                    while (resultSet.next()) {
                        items.add(Category(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("monthSummary"),
                                resultSet.getBigDecimal("yearSummary")
                        ))
                    }
                }
            }
        }
        return items
    }

    fun getCategoryDetails(id: Long): List<CategoryDetails> {
        val items = ArrayList<CategoryDetails>()
        val sql = getQuery(GET_CATEGORY_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1, id)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        items.add(CategoryDetails(
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("cena")
                        ))
                    }
                }
            }
        }
        return items
    }

    fun getAllShops(): List<Shop> {
        val items = ArrayList<Shop>()
        val sql = getQuery(GET_SHOP_LIST)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.createStatement().use { statement ->
                statement.executeQuery(sql).use { resultSet ->
                    while (resultSet.next()) {
                        items.add(Shop(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("monthSum"),
                                resultSet.getBigDecimal("yearSum")
                        ))
                    }
                }
            }
        }
        return items
    }

    fun getShopMonthItems(id: Long): List<ShoppingItem> {
        val sql = getQuery(GET_SHOP_MONTH_ITEMS)
        return getShoppingItems(sql, id)
    }

    fun getShopYearItems(shopId: Long): List<ShoppingItem> {
        val sql = getQuery(GET_SHOP_YEAR_ITEMS)
        return getShoppingItems(sql, shopId)
    }

    fun getShoppingItems(query: String, id: Long? = null): List<ShoppingItem> {
        val items = ArrayList<ShoppingItem>()
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(query).use { statement ->
                if (id != null) {
                    statement.setLong(1, id)
                }
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        mapToShoppingItem(items, resultSet)
                    }
                }
            }
        }
        return items
    }

    fun getItemById(itemId: Long): ShoppingItem {
        return ShoppingItem(1,"", BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE,"")
    }

    fun getMonthSummaryChartData(month: Int): List<ChartData>{
        val list = ArrayList<ChartData>()
        val sql = getQuery(GET_MONTH_SUMMARY_CHART_DATA)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, month)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ChartData(
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("suma")
                        ))
                    }
                }
            }
        }
        return list
    }
}