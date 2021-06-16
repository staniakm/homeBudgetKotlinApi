package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetailsRowMapper
import com.example.demo.entity.CategoryRowMapper
import com.example.demo.repository.SqlQueries.GET_CATEGORY_BY_ID
import com.example.demo.repository.SqlQueries.GET_CATEGORY_DETAILS
import com.example.demo.repository.SqlQueries.GET_CATEGORY_SUMMARY_LIST
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CategoryRepository(private val helper: RepositoryHelper) {

    fun getCategoriesSummary(date: LocalDate): List<Category> {
        return helper.getList(GET_CATEGORY_SUMMARY_LIST, CategoryRowMapper) {
            Category.bindByDate(date, this)
        }
    }

    fun getCategoryWithDetails(id: Long, date: LocalDate): Category? {
        return getCategoryById(id, date)?.apply {
            this.details = helper.getList(GET_CATEGORY_DETAILS, CategoryDetailsRowMapper) {
                bind(0, date)
                bind(1, date)
                bind(2, id)
            }
        }
    }

    private fun getCategoryById(id: Long, date: LocalDate): Category? {
        return helper.findFirstOrNull(GET_CATEGORY_BY_ID, CategoryRowMapper) {
            bind(0, date)
            bind(1, date)
            bind(2, date)
            bind(3, id)
        }
    }
}
