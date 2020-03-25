package com.example.demo.repository

import com.example.demo.entity.ProductDetails
import com.example.demo.entity.ProductDetailsRowMapper
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_PRODUCT_DETAILS
import com.example.demo.repository.SqlQueries.getQuery
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.sql.SQLException

@Repository
class ProductRepository(private val jdbi: Jdbi) {

    fun getProductDetails(productId: Long): List<ProductDetails> {

        return jdbi.withHandle<List<ProductDetails>, SQLException> { handle ->
            handle.createQuery(getQuery(GET_PRODUCT_DETAILS))
                    .bind(0, productId)
                    .map(ProductDetailsRowMapper())
                    .list()
        }
    }
}
