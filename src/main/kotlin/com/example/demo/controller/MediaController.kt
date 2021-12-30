package com.example.demo.controller

import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.service.MediaService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/media")
class MediaController(private val mediaService: MediaService) {

    @PostMapping("type")
    fun createMediaType(@RequestBody request: MediaTypeRequest): Mono<MediaType> {
        return mediaService.registerNewMediaType(request)
    }

    @GetMapping("/type/all")
    fun getAllMediaTypes() = mediaService.getAll()

    @GetMapping("type/{id}")
    fun getMediaTypeById(@PathVariable id: Int) = mediaService.getMediaTypeById(id)

}