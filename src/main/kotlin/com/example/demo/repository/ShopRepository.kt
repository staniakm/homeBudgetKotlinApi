package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.QUERY_TYPE.*
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.time.LocalDate

@Repository
class ShopRepository(private val jdbi: Jdbi) {

    fun getAllShopsSummary(date: LocalDate): List<ShopSummary> {

        return jdbi.withHandle<List<ShopSummary>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_SHOP_LIST_SUMMARY))
                    .bind(0, date.year)
                    .bind(1, date.year)
                    .bind(2, date.monthValue)
                    .map(ShopSummaryRowMapper())
                    .list()
        }
    }

    fun getShopMonthItems(id: Long): List<ShopItemsSummary> {
        val sql = getQuery(GET_SHOP_MONTH_ITEMS)
        return getShoppingItems(sql, id)
    }

    fun getShopYearItems(shopId: Long): List<ShopItemsSummary> {
        val sql = getQuery(GET_SHOP_YEAR_ITEMS)
        return getShoppingItems(sql, shopId)
    }

    fun getShopItems(shopId: Long): List<ShopItem> {
        return jdbi.withHandle<List<ShopItem>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_SHOP_ITEMS))
                    .bind(0, shopId)
                    .map(ShopItemRowMapper())
                    .list()
        }
    }

    fun getShoppingItems(query: String, id: Long): List<ShopItemsSummary> {

        return jdbi.withHandle<List<ShopItemsSummary>, SQLException> { handle ->
            handle.createQuery(query)
                    .bind(0, id)
                    .map(ShopItemSummaryRowMapper())
                    .list()
        }
    }

    fun getAllShops(): List<Shop> {
        return jdbi.withHandle<List<Shop>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_SHOP_LIST))
                    .map(ShopRowMapper())
                    .list()
        }
    }
}
