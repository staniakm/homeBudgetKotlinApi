package com.example.demo.service

import com.example.demo.entity.Assortment
import com.example.demo.repository.AssortmentRepository
import org.springframework.stereotype.Service

@Service
class AssortmentService(private val assortmentRepository: AssortmentRepository,
                        private val categoryService: CategoryService) {
    fun getAssortmentDetails(assortmentId: Long): Assortment? {
        return assortmentRepository.getAssortmentById(assortmentId)
    }

    fun changeAssortmentCategory(assortmentId: Long, categoryId: Long): Assortment? {
        return assortmentRepository.getAssortmentById(assortmentId)?.let { assortment ->
            return if (assortment.categoryId == categoryId) {
                assortment
            } else {
                categoryService.getCategoryById(categoryId)?.let {
                    assortmentRepository.updateAssortmentCategory(assortmentId, categoryId)
                    getAssortmentDetails(assortmentId)
                }
            }
        } ?: return null
    }
}
