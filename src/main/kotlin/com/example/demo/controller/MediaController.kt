package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.MediaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/media")
class MediaController(private val mediaService: MediaService) {

    @PostMapping("/type")
    fun createMediaType(@RequestBody request: MediaTypeRequest): ResponseEntity<MediaType> {
        return ResponseEntity.ok(mediaService.registerNewMediaType(request))
    }

    @GetMapping("/type/all")
    fun getAllMediaTypes() = mediaService.getAll()

    @GetMapping("type/{id}")
    fun getMediaTypeById(@PathVariable id: Int) = mediaService.getMediaTypeById(id)

    @GetMapping("/usage")
    fun getMediaUsageForMonth(@RequestParam("month", defaultValue = "0") month: Long): ResponseEntity<List<MediaItem>> {
        return ResponseEntity.ok(mediaService.getMediaForMonth(month))
    }

    @GetMapping("/usage/{mediaTypeId}")
    fun getMediaUsageByType(@PathVariable mediaTypeId: Int) = mediaService.getMediaUsageByType(mediaTypeId)

    @PostMapping("/usage")
    fun registerNewMediaUsage(@RequestBody mediaRegisterRequest: MediaRegisterRequest):ResponseEntity<List<MediaUsageResponse>> {
        return ResponseEntity.ok(mediaService.registerNewMediaUsage(mediaRegisterRequest))
    }

    @DeleteMapping("/usage/{mediaUsageId}")
    fun deleteMediaUsage(@PathVariable mediaUsageId: Int): ResponseEntity<Unit> {
        mediaService.deleteMediaUsage(mediaUsageId)
        return ResponseEntity.ok().build()
    }

}