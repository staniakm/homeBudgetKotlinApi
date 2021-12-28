package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.categoryDetailsRowMapper
import com.example.demo.entity.categoryRowMapper
import com.example.demo.repository.SqlQueries.GET_CATEGORY_BY_ID
import com.example.demo.repository.SqlQueries.GET_CATEGORY_DETAILS
import com.example.demo.repository.SqlQueries.GET_CATEGORY_SUMMARY_LIST
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class CategoryRepository(private val helper: RepositoryHelper) {

    fun getCategoriesSummary(date: LocalDate): Flux<Category> {
        return helper.getList(GET_CATEGORY_SUMMARY_LIST, categoryRowMapper) {
            Category.bindByDate(date, this)
        }
    }

    fun getCategory(id: Long, date: LocalDate): Mono<Category> {
        return getCategoryById(id, date)
    }

    fun getProductsForCategoryAndMonth(categoryId: Long, date: LocalDate): Flux<CategoryDetails> {
        return helper.getList(GET_CATEGORY_DETAILS, categoryDetailsRowMapper) {
            bind("$1", date.withDayOfMonth(1))
                .bind("$2", date.withDayOfMonth(date.lengthOfMonth()))
                .bind("$3", categoryId)
        }
    }

    private fun getCategoryById(id: Long, date: LocalDate): Mono<Category> {
        return helper.findFirstOrNull(GET_CATEGORY_BY_ID, categoryRowMapper) {
            bind("$1", date.year)
                .bind("$2", date.monthValue)
                .bind("$3", id)
        }
    }
}
