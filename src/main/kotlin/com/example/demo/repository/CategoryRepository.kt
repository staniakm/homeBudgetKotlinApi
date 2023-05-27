package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_CATEGORY_DETAILS
import com.example.demo.repository.SqlQueries.GET_CATEGORY_SUMMARY_BY_ID_AND_MONTH
import com.example.demo.repository.SqlQueries.GET_CATEGORY_SUMMARY_LIST
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate

@Repository
class CategoryRepository(private val helper: RepositoryHelper) {

    fun getCategoriesSummary(date: LocalDate): List<CategorySummary> {
        return helper.jdbcQueryGetList(GET_CATEGORY_SUMMARY_LIST, {
            setInt(1, date.year)
            setInt(2, date.year)
            setInt(3, date.monthValue)
        }, categorySummaryRowMapper)
    }

    fun getCategory(id: Long, date: LocalDate): CategorySummary? {
        return getCategorySummaryByIdAndMonth(id, date)
    }

    fun getProductsForCategoryAndMonth(categoryId: Long, date: LocalDate): List<CategoryDetails> {
        return helper.jdbcQueryGetList(GET_CATEGORY_DETAILS, {
            setDate(1, Date.valueOf(date.withDayOfMonth(1)))
            setDate(2, Date.valueOf(date.withDayOfMonth(date.lengthOfMonth())))
            setLong(3, categoryId)
        }, categoryDetailsRowMapper)
    }

    private fun getCategorySummaryByIdAndMonth(id: Long, date: LocalDate): CategorySummary? {
        return helper.jdbcQueryGetFirst(GET_CATEGORY_SUMMARY_BY_ID_AND_MONTH, {
            setInt(1, date.year)
            setInt(2, date.year)
            setInt(3, date.monthValue)
            setLong(4, id)
        }, categorySummaryRowMapper)
    }

    fun getCategoryById(categoryId: Long): Category? {
        return helper.jdbcQueryGetFirst(SqlQueries.GET_CATEGORY_BY_ID, {
            setInt(1, categoryId.toInt())
        }, categorySingleItemMapper)
    }
}
