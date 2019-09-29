package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_CATEGORY_DETAILS
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_CATEGORY_SUMMARY_LIST
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.sql.DriverManager
import java.time.LocalDate

@Repository
class CategoryRepository {
    @Value("\${sql.server.url}")
    private val connectionUrl: String? = null

    fun getCategoriesSummary(date: LocalDate): List<Category> {
        val items = ArrayList<Category>()
        val sql = SqlQueries.getQuery(GET_CATEGORY_SUMMARY_LIST)
        DriverManager.getConnection(connectionUrl).use { con ->
            con.prepareStatement(sql).use { statement ->
                statement.setInt(1, date.year)
                statement.setInt(2, date.year)
                statement.setInt(3, date.monthValue)
                statement.executeQuery().use { resultSet ->
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
        val sql = SqlQueries.getQuery(GET_CATEGORY_DETAILS)
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
        val sql = SqlQueries.getQuery(SqlQueries.QUERY_TYPE.GET_CATEGORY_BY_ID)
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
}