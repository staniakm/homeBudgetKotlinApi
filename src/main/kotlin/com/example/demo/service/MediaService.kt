package com.example.demo.service

import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.repository.MediaRepository
import com.example.demo.repository.MediaTypeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class MediaService(
    private val mediaTypeRepository: MediaTypeRepository,
    private val mediaRepository: MediaRepository,
    private val clock: ClockProvider
) {
    fun registerNewMediaType(request: MediaTypeRequest) = mediaTypeRepository.registerNewMediaType(request.mediaName)
    fun getAll() = mediaTypeRepository.findAll()
    fun getMediaTypeById(id: Int) = mediaTypeRepository.findById(id)
    fun registerNewMediaUsage(mediaRegisterRequest: MediaRegisterRequest): Flux<MediaItem> {
        return getMediaTypeById(mediaRegisterRequest.mediaType)
            .flatMapMany {
                mediaRepository.createMediaUsage(
                    it.id,
                    mediaRegisterRequest.meterRead,
                    mediaRegisterRequest.year,
                    mediaRegisterRequest.month
                )
            }
    }

    fun getMediaForMonth(month: Long) = clock.getDateFromMonth(month)
        .let {
            mediaRepository.getMediaForMonth(it.year, it.monthValue)
        }
}
