package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_SHOP_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST_SUMMARY
import com.example.demo.repository.SqlQueries.GET_SHOP_MONTH_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_YEAR_ITEMS
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ShopRepository(private val helper: RepositoryHelper) {

    fun getAllShopsSummary(date: LocalDate): List<ShopSummary> {
        return helper.getList(GET_SHOP_LIST_SUMMARY, ShopSummaryRowMapper) {
            bind(0, date.year)
            bind(1, date.year)
            bind(2, date.monthValue)
        }
    }

    fun getShopMonthItems(id: Long, date: LocalDate): List<ShopItemsSummary> {
        return helper.getList(GET_SHOP_MONTH_ITEMS, ShopItemSummaryRowMapper()) {
            bind(0, id)
            bind(1, date)
            bind(2, date)
        }
    }

    fun getShopYearItems(shopId: Long, date: LocalDate): List<ShopItemsSummary> {
        return helper.getList(GET_SHOP_YEAR_ITEMS, ShopItemSummaryRowMapper()) {
            bind(0, shopId)
            bind(1, date)
        }
    }

    fun getShopItems(shopId: Long): List<ShopItem> {
        return helper.getList(GET_SHOP_ITEMS, ShopItemRowMapper()) {
            bind(0, shopId)
        }
    }

    fun getAllShops(): List<Shop> = helper.getList(GET_SHOP_LIST, ShopRowMapper())
}
