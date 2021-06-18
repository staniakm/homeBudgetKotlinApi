package com.example.demo.repository

import com.example.demo.entity.ProductDetails
import com.example.demo.entity.ProductDetailsRowMapper
import com.example.demo.repository.SqlQueries.GET_PRODUCT_DETAILS
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class ProductRepository(private val helper: RepositoryHelper) {

    fun getProductDetails(productId: Long): Flux<ProductDetails> {
        return helper.getList(GET_PRODUCT_DETAILS, ProductDetailsRowMapper.map) {
            bind("id", productId)
        }
    }
}
