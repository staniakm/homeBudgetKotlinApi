package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.categoryDetailsRowMapper
import com.example.demo.entity.categoryRowMapper
import com.example.demo.repository.SqlQueries.GET_CATEGORY_BY_ID
import com.example.demo.repository.SqlQueries.GET_CATEGORY_DETAILS
import com.example.demo.repository.SqlQueries.GET_CATEGORY_SUMMARY_LIST
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate

@Repository
class CategoryRepository(private val helper: RepositoryHelper) {

    fun getCategoriesSummary(date: LocalDate): List<Category> {
        return helper.jdbcQueryGetList(GET_CATEGORY_SUMMARY_LIST, {
            setInt(1, date.year)
            setInt(2, date.year)
            setInt(3, date.monthValue)
        }, categoryRowMapper)
    }

    fun getCategory(id: Long, date: LocalDate): Category? {
        return getCategoryById(id, date)
    }

    fun getProductsForCategoryAndMonth(categoryId: Long, date: LocalDate): List<CategoryDetails> {
        return helper.jdbcQueryGetList(GET_CATEGORY_DETAILS, {
            setDate(1, Date.valueOf(date.withDayOfMonth(1)))
            setDate(2, Date.valueOf(date.withDayOfMonth(date.lengthOfMonth())))
            setLong(3, categoryId)
        }, categoryDetailsRowMapper)
    }

    private fun getCategoryById(id: Long, date: LocalDate): Category? {
        return helper.jdbcQueryGetFirst(GET_CATEGORY_BY_ID, {
            setInt(1, date.year)
            setInt(2, date.year)
            setInt(3, date.monthValue)
            setLong(4, id)
        }, categoryRowMapper)
    }
}
