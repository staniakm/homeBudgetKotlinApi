package com.example.demo.service

import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.entity.MediaUsageResponse
import com.example.demo.repository.MediaRepository
import com.example.demo.repository.MediaTypeRepository
import org.springframework.stereotype.Service

@Service
class MediaService(
    private val mediaTypeRepository: MediaTypeRepository,
    private val mediaRepository: MediaRepository,
    private val clock: ClockProvider
) {
    fun registerNewMediaType(request: MediaTypeRequest) = mediaTypeRepository.registerNewMediaType(request.mediaName)
    fun getAll() = mediaTypeRepository.findAll()
    fun getMediaTypeById(id: Int): MediaType? = mediaTypeRepository.findById(id)
    fun registerNewMediaUsage(mediaRegisterRequest: MediaRegisterRequest): List<MediaUsageResponse> {
        return getMediaTypeById(mediaRegisterRequest.mediaType)
            ?.let {
                mediaRepository.createMediaUsage(
                    it.id,
                    mediaRegisterRequest.meterRead,
                    mediaRegisterRequest.year,
                    mediaRegisterRequest.month
                )
            }?.map {
                it.toResponse()
            } ?: listOf()

    }

    fun getMediaForMonth(month: Long) = clock.getDateFromMonth(month)
        .let {
            mediaRepository.getMediaForMonth(it.year, it.monthValue)
        }

    fun getMediaUsageByType(mediaTypeId: Int): List<MediaUsageResponse> {
        return mediaTypeRepository.findById(mediaTypeId)
            ?.let {
                mediaRepository.findByMediaType(it.id).map { it.toResponse() }
            } ?: listOf()
    }

    fun deleteMediaUsage(mediaUsageId: Int): Int {
        return mediaRepository.deleteMediaUsageEntry(mediaUsageId)
    }
}
