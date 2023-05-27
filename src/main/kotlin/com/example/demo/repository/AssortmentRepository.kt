package com.example.demo.repository

import com.example.demo.entity.Assortment
import com.example.demo.entity.assortmentSingleItemMapper
import org.springframework.stereotype.Repository

@Repository
class AssortmentRepository(private val helper: RepositoryHelper) {
    fun getAssortmentById(assortmentId: Long): Assortment? {
        return helper.jdbcQueryGetFirst(SqlQueries.GET_SINGLE_ASSORTMENT_BY_ID, {
            setLong(1, assortmentId)
        }, assortmentSingleItemMapper)
    }

    fun updateAssortmentCategory(assortmentId: Long, categoryId: Long) {
        helper.updateJdbc(SqlQueries.UPDATE_ASSORTMENT_CATEGORY) {
            setLong(1, categoryId)
            setLong(2, assortmentId)
        }
    }
}