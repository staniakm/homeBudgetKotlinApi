package com.example.demo.repository

import com.example.demo.entity.ProductHistory
import com.example.demo.entity.productHistoryRowMapper
import com.example.demo.repository.SqlQueries.GET_PRODUCT_HISTORY
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class ProductRepository(private val helper: RepositoryHelper) {

    fun getProductHistory(productId: Long): Flux<ProductHistory> {
        return helper.getList(GET_PRODUCT_HISTORY, productHistoryRowMapper) {
            bind("$1", productId)
        }
    }
}
