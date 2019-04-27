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
                                resultSet.getBigDecimal("yearSummary"),
                                emptyList()
                        ))
                    }
                }
            }
        }
        return items
    }

    fun getCategoryDetails(id: Long): Category? {
        val category = getCategory(id)
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
        category?.details = items
        return category
    }

    private fun getCategory(id: Long): Category? {
        var cat: Category? = null
        val sql = getQuery(GET_CATEGORY_BY_ID)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setLong(1, id)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        cat = Category(
                                resultSet.getLong("id"),
                                resultSet.getString("nazwa"),
                                resultSet.getBigDecimal("monthSummary"),
                                resultSet.getBigDecimal("yearSummary"),
                                emptyList()
                        )
                    }
                }
            }
        }
        return cat
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

    fun getShopMonthItems(id: Long): List<ShopItemsSummary> {
        val sql = getQuery(GET_SHOP_MONTH_ITEMS)
        return getShoppingItems(sql, id)
    }

    fun getShopYearItems(shopId: Long): List<ShopItemsSummary> {
        val sql = getQuery(GET_SHOP_YEAR_ITEMS)
        return getShoppingItems(sql, shopId)
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

    fun getMonthSummaryChartData(month: Int): List<ChartData> {
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

    fun getShopItems(shopId: Long): List<ShopItem> {
        val list = ArrayList<ShopItem>()
        val sql = getQuery(GET_SHOP_ITEMS)
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

    fun getBudgetForMonth(date: LocalDate): BudgetItem {
        val list = ArrayList<MonthBudget>()
        val sql = getQuery(GET_MONTH_BUDGET)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        list.add(MonthBudget(
                                resultSet.getString("category"),
                                resultSet.getBigDecimal("spent"),
                                resultSet.getBigDecimal("planned"),
                                resultSet.getDouble("percentage")
                        ))
                    }
                }
            }
        }
        return getBudgetCalculations(date, list)
    }

    fun getBudgetCalculations(date: LocalDate, list: ArrayList<MonthBudget>): BudgetItem {
        val budget = BudgetItem(date.toString().substring(0, 7), list)
        val sql = getQuery(GET_MONTH_BUDGE_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.monthValue)
                statement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        budget.totalSpend = resultSet.getBigDecimal("outcome")
                        budget.totalPlanned = resultSet.getBigDecimal("planed")
                        budget.totalEarned = resultSet.getBigDecimal("income")
                    }
                }
            }
        }
        return budget
    }

    fun updateBudget(date: LocalDate, monthBudget: MonthBudgetDto) {
        val cat: String = monthBudget.category;
        val planned: BigDecimal = monthBudget.planned;
        val year: Int = date.year;
        val month: Int = date.monthValue


        val sql = getQuery(UPDATE_MONTH_BUDGE_DETAILS)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setString(2, cat)
                statement.setBigDecimal(1, planned)
                statement.setInt(3, year)
                statement.setInt(4, month)
                statement.executeUpdate()
            }
        }
        recalculateBudget(date)
    }

    fun recalculateBudget(date: LocalDate) {
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareCall("{call dbo.RecalculateBudget (?)}")
                    .use { stmt ->
                        stmt.setDate(1, java.sql.Date.valueOf(date))
                        stmt.execute()
                    }
        }
    }

    fun getProductDetails(productId: Long): List<ProductDetails> {
        val list = ArrayList<ProductDetails>()
        val sql = getQuery(GET_PRODUCT_DETAILS)
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