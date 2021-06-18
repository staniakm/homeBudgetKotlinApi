package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.CategoryDetailsRowMapper
import com.example.demo.entity.CategoryRowMapper
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
        return helper.getList(GET_CATEGORY_SUMMARY_LIST, CategoryRowMapper.map) {
            Category.bindByDate(date, this)
        }
    }

    fun getCategory(id: Long, date: LocalDate): Mono<Category>? {

        return getCategoryById(id, date)
    }

    fun getCategoryDetails(id: Long, date: LocalDate): Flux<CategoryDetails> {
        return helper.getList(GET_CATEGORY_DETAILS, CategoryDetailsRowMapper.map) {
            bind(0, date)
                .bind("date", date)
                .bind("id", id)
        }
    }

    private fun getCategoryById(id: Long, date: LocalDate): Mono<Category> {
        return helper.findFirstOrNull(GET_CATEGORY_BY_ID, CategoryRowMapper.map) {
            bind("date", date)
                .bind("id", id)
        }
    }
}
