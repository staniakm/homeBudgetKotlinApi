package com.example.demo.service

import com.example.demo.FakeClockProvider
import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaUsage
import com.example.demo.repository.MediaRepository
import com.example.demo.repository.MediaTypeRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@Tag("unit")
class MediaServiceTest {

    @Test
    fun `should return empty media usage when media type does not exist`() {
        val mediaTypeRepository = mock(MediaTypeRepository::class.java)
        val mediaRepository = mock(MediaRepository::class.java)
        `when`(mediaTypeRepository.findById(10)).thenReturn(null)

        val service = MediaService(mediaTypeRepository, mediaRepository, FakeClockProvider())
        val result = service.getMediaUsageByType(10)

        result shouldBe emptyList()
    }

    @Test
    fun `should return mapped media usage for existing media type`() {
        val mediaTypeRepository = mock(MediaTypeRepository::class.java)
        val mediaRepository = mock(MediaRepository::class.java)
        `when`(mediaTypeRepository.findById(1)).thenReturn(MediaType(1, "WATER"))
        `when`(mediaRepository.findByMediaType(1)).thenReturn(listOf(MediaUsage(1, 1, 2022, 5, 12.5)))

        val service = MediaService(mediaTypeRepository, mediaRepository, FakeClockProvider())
        val result = service.getMediaUsageByType(1)

        result.size shouldBe 1
        result.first().meterRead shouldBe 12.5
    }

    @Test
    fun `should register usage and map to response`() {
        val mediaTypeRepository = mock(MediaTypeRepository::class.java)
        val mediaRepository = mock(MediaRepository::class.java)
        `when`(mediaTypeRepository.findById(1)).thenReturn(MediaType(1, "POWER"))
        `when`(mediaRepository.createMediaUsage(1, 100.0, 2022, 11))
            .thenReturn(listOf(MediaUsage(1, 1, 2022, 11, 100.0)))

        val service = MediaService(mediaTypeRepository, mediaRepository, FakeClockProvider())
        val result = service.registerNewMediaUsage(MediaRegisterRequest(1, 100.0, 2022, 11))

        result.size shouldBe 1
        result.first().year shouldBe 2022
        result.first().meterRead shouldBe 100.0
    }
}
