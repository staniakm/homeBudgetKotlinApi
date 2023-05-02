package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.MediaService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/api/media")
class MediaController(private val mediaService: MediaService) {

    @PostMapping("/type")
    fun createMediaType(@RequestBody request: MediaTypeRequest): Mono<MediaType> {
        return mediaService.registerNewMediaType(request).toMono()
    }

    @GetMapping("/type/all")
    fun getAllMediaTypes() = mediaService.getAll()

    @GetMapping("type/{id}")
    fun getMediaTypeById(@PathVariable id: Int) = mediaService.getMediaTypeById(id)

    @GetMapping("/usage")
    fun getMediaUsageForMonth(@RequestParam("month", defaultValue = "0") month: Long): Flux<MediaItem> {
        return mediaService.getMediaForMonth(month).toFlux()
    }

    @GetMapping("/usage/{mediaTypeId}")
    fun getMediaUsageByType(@PathVariable mediaTypeId: Int) = mediaService.getMediaUsageByType(mediaTypeId)

    @PostMapping("/usage")
    fun registerNewMediaUsage(@RequestBody mediaRegisterRequest: MediaRegisterRequest): Flux<MediaUsageResponse> {
        return mediaService.registerNewMediaUsage(mediaRegisterRequest).toFlux()
    }

    @DeleteMapping("/usage/{mediaUsageId}")
    fun deleteMediaUsage(@PathVariable mediaUsageId: Int): Mono<Void> {
        mediaService.deleteMediaUsage(mediaUsageId)
        return Mono.empty()
    }

}