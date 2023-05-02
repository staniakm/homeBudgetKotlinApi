package com.example.demo.repository

import com.example.demo.entity.*
import com.example.demo.repository.SqlQueries.GET_PRODUCT_BY_ID
import com.example.demo.repository.SqlQueries.GET_PRODUCT_HISTORY
import com.example.demo.repository.SqlQueries.UPDATE_PRODUCT_CATEGORY
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val helper: RepositoryHelper) {
    fun getProductHistory(productId: Long): List<ProductHistory> {
        return helper
            .jdbcQueryGetList(GET_PRODUCT_HISTORY, { setLong(1, productId) }, productHistoryRowMapperJdbc)
    }

    fun getProduct(productId: Long): Product? = helper
        .jdbcQueryGetFirst(GET_PRODUCT_BY_ID, { setLong(1, productId) }, productRowMapper)

    fun updateCategory(id: Long, categoryId: Long): Unit {
        return helper
            .updateJdbc(
                UPDATE_PRODUCT_CATEGORY
            ) {
                setLong(1, categoryId)
                setLong(2, id)
            }.let {
                println("Updated $it rows")
            }
    }
}
