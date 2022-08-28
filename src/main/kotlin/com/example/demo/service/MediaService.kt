package com.example.demo.service

import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.entity.MediaUsageResponse
import com.example.demo.repository.MediaRepository
import com.example.demo.repository.MediaTypeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MediaService(
    private val mediaTypeRepository: MediaTypeRepository,
    private val mediaRepository: MediaRepository,
    private val clock: ClockProvider
) {
    fun registerNewMediaType(request: MediaTypeRequest) = mediaTypeRepository.registerNewMediaType(request.mediaName)
    fun getAll() = mediaTypeRepository.findAll()
    fun getMediaTypeById(id: Int) = mediaTypeRepository.findById(id)
    fun registerNewMediaUsage(mediaRegisterRequest: MediaRegisterRequest): Flux<MediaUsageResponse> {
        return getMediaTypeById(mediaRegisterRequest.mediaType)
            .flatMapMany {
                mediaRepository.createMediaUsage(
                    it.id,
                    mediaRegisterRequest.meterRead,
                    mediaRegisterRequest.year,
                    mediaRegisterRequest.month
                )
            }.map {
                it.toResponse()
            }
    }

    fun getMediaForMonth(month: Long) = clock.getDateFromMonth(month)
        .let {
            mediaRepository.getMediaForMonth(it.year, it.monthValue)
        }

    fun getMediaUsageByType(mediaTypeId: Int): Flux<MediaUsageResponse> {
        return mediaTypeRepository.findById(mediaTypeId)
            .flatMapMany {
                mediaRepository.findByMediaType(it.id)
            }.map {
                it.toResponse()
            }
    }

    fun deleteMediaUsage(mediaUsageId: Int): Mono<Void> {
        return mediaRepository.deleteMediaUsageEntry(mediaUsageId)
    }
}
