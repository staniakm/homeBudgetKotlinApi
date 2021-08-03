package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_SHOP_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST_SUMMARY
import com.example.demo.repository.SqlQueries.GET_SHOP_MONTH_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_YEAR_ITEMS
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDate

@Repository
class ShopRepository(private val helper: RepositoryHelper) {

    fun getAllShopsSummary(date: LocalDate): Flux<ShopSummary> {
        return helper.getList(GET_SHOP_LIST_SUMMARY, shopSummaryRowMapper) {
            bind("year", date.year).bind("month", date.monthValue)
        }
    }

    fun getShopMonthItems(id: Long, date: LocalDate): Flux<ShopItemsSummary> {
        return helper.getList(GET_SHOP_MONTH_ITEMS, shopItemSummaryRowMapper) {
            bind("id", id)
                .bind("date", date)
        }
    }

    fun getShopYearItems(shopId: Long, date: LocalDate): Flux<ShopItemsSummary> {
        return helper.getList(GET_SHOP_YEAR_ITEMS, shopItemSummaryRowMapper) {
            bind("id", shopId)
                .bind("date", date)
        }
    }

    fun getShopItems(shopId: Long): Flux<ShopItem> {
        return helper.getList(GET_SHOP_ITEMS, shopItemRowMapper) {
            bind("id", shopId)
        }
    }

    fun getAllShops(): Flux<Shop> = helper.getList(GET_SHOP_LIST, shopRowMapper)
}
