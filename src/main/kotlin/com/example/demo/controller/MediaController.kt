package com.example.demo.controller

import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.service.MediaService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/media")
class MediaController(private val mediaService: MediaService) {

    @PostMapping("/type")
    fun createMediaType(@RequestBody request: MediaTypeRequest): Mono<MediaType> {
        return mediaService.registerNewMediaType(request)
    }

    @GetMapping("/type/all")
    fun getAllMediaTypes() = mediaService.getAll()

    @GetMapping("type/{id}")
    fun getMediaTypeById(@PathVariable id: Int) = mediaService.getMediaTypeById(id)

    @GetMapping("/usage")
    fun getMediaUsageForMonth(@RequestParam("month", defaultValue = "0") month: Long): Flux<MediaItem> {
        return mediaService.getMediaForMonth(month)
    }

    @PostMapping("/usage")
    fun registerNewMediaUsage(@RequestBody mediaRegisterRequest: MediaRegisterRequest): Flux<MediaItem> {
        return mediaService.registerNewMediaUsage(mediaRegisterRequest)
    }

}