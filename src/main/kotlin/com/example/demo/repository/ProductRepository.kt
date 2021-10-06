package com.example.demo.repository

import com.example.demo.entity.ProductDetails
import com.example.demo.entity.productDetailsRowMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class ProductRepository(private val helper: RepositoryHelper,
                        private val queryProvider: QueryProvider) {

    fun getProductDetails(productId: Long): Flux<ProductDetails> {
        return helper.getList(queryProvider.GET_PRODUCT_DETAILS, productDetailsRowMapper) {
            bind("id", productId)
        }
    }
}
