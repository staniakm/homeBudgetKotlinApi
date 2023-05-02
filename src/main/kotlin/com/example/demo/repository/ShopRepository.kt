package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.CREATE_SHOP
import com.example.demo.repository.SqlQueries.GET_SHOP_BY_NAME
import com.example.demo.repository.SqlQueries.GET_SHOP_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_ITEM_BY_NAME
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST
import com.example.demo.repository.SqlQueries.GET_SHOP_LIST_SUMMARY
import com.example.demo.repository.SqlQueries.GET_SHOP_MONTH_ITEMS
import com.example.demo.repository.SqlQueries.GET_SHOP_YEAR_ITEMS
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate

@Repository
class ShopRepository(private val helper: RepositoryHelper) {

    fun getAllShopsMonthSummary(date: LocalDate): List<ShopSummary> {
        return helper.jdbcQueryGetList(GET_SHOP_LIST_SUMMARY, {
            setInt(1, date.year)
            setInt(2, date.year)
            setInt(3, date.monthValue)
        }, shopSummaryRowMapperJdbc)
    }

    fun getShopMonthItems(id: Long, date: LocalDate): List<ShopItemsSummary> {
        return helper.jdbcQueryGetList(GET_SHOP_MONTH_ITEMS, {
            setLong(1, id)
            setDate(2, Date.valueOf(date.withDayOfMonth(1)))
            setDate(3, Date.valueOf(date.withDayOfMonth(date.lengthOfMonth())))
        }, shopItemSummaryRowMapperJdbc)
    }

    fun getShopYearItems(shopId: Long, date: LocalDate): List<ShopItemsSummary> {
        return helper.jdbcQueryGetList(GET_SHOP_YEAR_ITEMS, {
            setLong(1, shopId)
            setInt(2, date.year)
        }, shopItemSummaryRowMapperJdbc)
    }

    fun getShopItems(shopId: Long): List<ShopItem> {
        return helper.jdbcQueryGetList(GET_SHOP_ITEMS, {
            setLong(1, shopId)
        }, shopItemRowMapperJdbc)
    }

    fun getAllShops(): List<Shop> = helper.jdbcQueryGetList(GET_SHOP_LIST, {}, shopRowMapperJdbc)
    fun createShop(shopName: String): Shop {
        helper.updateJdbc(CREATE_SHOP) {
            setString(1, shopName)
        }
        return helper.jdbcQueryGetFirst(GET_SHOP_BY_NAME, {
            setString(1, shopName)
        }, shopRowMapperJdbc)!!
    }

    fun createShopItem(shopId: Int, name: String): ShopItem {
        helper.callProcedureJdbc("CALL addasotoshop(?, ?)") {
            setString(1, name.uppercase())
            setInt(2, shopId)
        }
        return helper.jdbcQueryGetFirst(GET_SHOP_ITEM_BY_NAME, {
            setInt(1, shopId)
            setString(2, name.uppercase())
        }, shopItemRowMapperJdbc)!!
    }
}
