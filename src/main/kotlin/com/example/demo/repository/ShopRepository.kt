package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_SHOP_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST_SUMMARY
import com.example.demo.repository.SqlQueries.GET_SHOP_MONTH_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_YEAR_ITEMS
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class ShopRepository(private val helper: RepositoryHelper) {

    fun getAllShopsSummary(date: LocalDate): Flux<ShopSummary> {
        return helper.getList(GET_SHOP_LIST_SUMMARY, shopSummaryRowMapper) {
            bind("$1", date.year).bind("$2", date.monthValue)
        }
    }

    fun getShopMonthItems(id: Long, date: LocalDate): Flux<ShopItemsSummary> {
        return helper.getList(GET_SHOP_MONTH_ITEMS, shopItemSummaryRowMapper) {
            bind("$1", id)
                .bind("$2", date.withDayOfMonth(1))
                .bind("$3", date.withDayOfMonth(date.lengthOfMonth()))
        }
    }

    fun getShopYearItems(shopId: Long, date: LocalDate): Flux<ShopItemsSummary> {
        return helper.getList(GET_SHOP_YEAR_ITEMS, shopItemSummaryRowMapper) {
            bind("$1", shopId)
                .bind("$2", date.year)
        }
    }

    fun getShopItems(shopId: Long): Flux<ShopItem> {
        return helper.getList(GET_SHOP_ITEMS, shopItemRowMapper) {
            bind("$1", shopId)
        }
    }

    fun getAllShops(): Flux<Shop> = helper.getList(GET_SHOP_LIST, shopRowMapper)
    fun createShop(shopName: String): Mono<Shop> {
        return helper.executeUpdate(SqlQueries.CREATE_SHOP) {
            bind("$1", shopName)
        }.then(helper.findOne(SqlQueries.GET_SHOP_BY_NAME, shopRowMapper) {
            bind("$1", shopName)
        })
    }

    fun createShopItem(shopId: Int, name: String): Mono<ShopItem> {
        return helper.callProcedure("call addasotoshop ($1, $2)") {
            bind("$1", name.uppercase()).bind("$2", shopId)
        }.then(helper.findOne(SqlQueries.GET_SHOP_ITEM_BY_NAME, shopItemRowMapper) {
            bind("$1", name.uppercase()).bind("$2", shopId)
        })
    }
}
