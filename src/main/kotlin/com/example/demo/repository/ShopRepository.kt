package com.example.demo.repository

import com.example.demo.entity.Shop
import com.example.demo.entity.ShopSummary
import com.example.demo.entity.ShopItem
import com.example.demo.entity.ShopItemsSummary
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_SHOP_LIST
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.sql.ResultSet
import java.time.LocalDate

@Repository
class ShopRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getAllShopsSummary(date: LocalDate): List<ShopSummary> {
        val items = ArrayList<ShopSummary>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_SHOP_LIST_SUMMARY)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.year)
                statement.setInt(3, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        items.add(ShopSummary(
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

    fun getShopMonthItems(id: Long): List<ShopItemsSummary> {
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_SHOP_MONTH_ITEMS)
        return getShoppingItems(sql, id)
    }

    fun getShopYearItems(shopId: Long): List<ShopItemsSummary> {
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_SHOP_YEAR_ITEMS)
        return getShoppingItems(sql, shopId)
    }

    fun getShopItems(shopId: Long): List<ShopItem> {
        val list = ArrayList<ShopItem>()
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_SHOP_ITEMS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1, shopId)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(ShopItem(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa")
                        ))
                    }
                }
            }
        }
        return list
    }

    fun getShoppingItems(query: String, id: Long? = null): List<ShopItemsSummary> {
        val items = ArrayList<ShopItemsSummary>()
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

    fun getAllShops(): List<Shop> {
        val shopList = arrayListOf<Shop>()
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(SqlQueries.getQuery(GET_SHOP_LIST)).use { ps ->
                ps.executeQuery().use { rs ->
                    while (rs.next()) {
                        shopList.add(
                                Shop(
                                        rs.getLong("id"),
                                        rs.getString("name")
                                )
                        )
                    }
                }
            }
        }
        return shopList
    }

    private fun mapToShoppingItem(items: ArrayList<ShopItemsSummary>, rs: ResultSet) {
        items.add(ShopItemsSummary(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("quantity"),
                rs.getBigDecimal("min_price_for_unit"),
                rs.getBigDecimal("max_price_for_unit"),
                rs.getBigDecimal("discount_sum"),
                rs.getBigDecimal("total_spend")
        ))
    }
}
