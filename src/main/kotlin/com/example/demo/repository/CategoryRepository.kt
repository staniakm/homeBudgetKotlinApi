package com.example.demo.repository

import com.example.demo.entity.Category
import com.example.demo.entity.CategoryDetails
import com.example.demo.entity.categoryDetailsRowMapper
import com.example.demo.entity.categoryRowMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class CategoryRepository(private val helper: RepositoryHelper,
                         private val queryProvider: QueryProvider) {

    fun getCategoriesSummary(date: LocalDate): Flux<Category> {
        return helper.getList(queryProvider.GET_CATEGORY_SUMMARY_LIST, categoryRowMapper) {
            Category.bindByDate(date, this)
        }
    }

    fun getCategory(id: Long, date: LocalDate): Mono<Category>? {

        return getCategoryById(id, date)
    }

    fun getCategoryDetails(id: Long, date: LocalDate): Flux<CategoryDetails> {
        return helper.getList(queryProvider.GET_CATEGORY_DETAILS, categoryDetailsRowMapper) {
            bind(0, date)
                    .bind("date", date)
                    .bind("id", id)
        }
    }

    private fun getCategoryById(id: Long, date: LocalDate): Mono<Category> {
        return helper.findFirstOrNull(queryProvider.GET_CATEGORY_BY_ID, categoryRowMapper) {
            bind("date", date)
                    .bind("id", id)
        }
    }
}
