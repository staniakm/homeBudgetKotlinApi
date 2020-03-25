package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.CategoryDetailsRowMapper
import com.example.demo.entity.CategoryRowMapper
import com.example.demo.repository.SqlQueries.QUERY_TYPE.*
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.sql.SQLException
import java.time.LocalDate

@Repository
class CategoryRepository(private val jdbi: Jdbi) {

    fun getCategoriesSummary(date: LocalDate): List<Category> {
        return jdbi.withHandle<List<Category>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_CATEGORY_SUMMARY_LIST))
                    .bind(0, date.year)
                    .bind(1, date.year)
                    .bind(2, date.monthValue)
                    .map(CategoryRowMapper())
                    .list()
        }
    }

    fun getCategoryDetails(id: Long, date: LocalDate): Category? {
        return getCategoryById(id).apply {
            this?.details = jdbi.withHandle<List<CategoryDetails>, SQLException> { handle ->
                handle.createQuery(getQuery(GET_CATEGORY_DETAILS))
                        .bind(0, id)
                        .map(CategoryDetailsRowMapper())
                        .list()
            }
        }
    }

    private fun getCategoryById(id: Long): Category? {
        return jdbi.withHandle<Category, SQLException> { handle ->
            handle.createQuery(getQuery(GET_CATEGORY_BY_ID))
                    .bind(0, id)
                    .map(CategoryRowMapper())
                    .one()
        }
    }
}
