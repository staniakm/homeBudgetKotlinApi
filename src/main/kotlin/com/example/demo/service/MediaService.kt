package com.example.demo.service

import com.example.demo.entity.MediaTypeRequest
import com.example.demo.repository.MediaTypeRepository
import org.springframework.stereotype.Service

@Service
class MediaService(private val mediaTypeRepository: MediaTypeRepository) {
    fun registerNewMediaType(request: MediaTypeRequest) = mediaTypeRepository.registerNewMediaType(request.mediaName)
    fun getAll() = mediaTypeRepository.findAll()
    fun getMediaTypeById(id: Int) = mediaTypeRepository.findById(id)

}
