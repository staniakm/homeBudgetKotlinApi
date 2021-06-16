package com.example.demo.repository

import com.example.demo.entity.ProductDetails
import com.example.demo.entity.ProductDetailsRowMapper
import com.example.demo.repository.SqlQueries.GET_PRODUCT_DETAILS
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val helper: RepositoryHelper) {

    fun getProductDetails(productId: Long): List<ProductDetails> {
        return helper.getList(GET_PRODUCT_DETAILS, ProductDetailsRowMapper) {
            bind(0, productId)
        }
    }
}
