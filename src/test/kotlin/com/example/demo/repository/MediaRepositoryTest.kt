package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.MediaItem
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class MediaRepositoryTest(@Autowired private val mediaRepository: MediaRepository) : IntegrationTest() {

    @Test
    fun `should fetch media for selected month`() {
        createMediaType(1, "POWER")
        createMediaType(2, "GAS")
        createMedia(id = 1, mediaTypeId = 1, meterRead = 11.00)
        createMedia(2, mediaTypeId = 2, meterRead = 123.00)

        val mediaListItems =
            mediaRepository.getMediaForMonth(LocalDate.now().year, LocalDate.now().monthValue).collectList().block()!!

        mediaListItems.size shouldBe 2
        mediaListItems shouldContainAll listOf(
            MediaItem(1, 1, LocalDate.now().year, LocalDate.now().monthValue, 11.00),
            MediaItem(2, 2, LocalDate.now().year, LocalDate.now().monthValue, 123.00)
        )
    }

    @Test
    fun `should register new media usage and return all media for month`() {
        createMediaType(1, "POWER")

        val mediaReads =
            mediaRepository.createMediaUsage(1, 123.01, LocalDate.now().year, LocalDate.now().monthValue).collectList()
                .block()!!

        mediaReads.size shouldBe 1
        mediaReads shouldContainAll listOf(MediaItem(1, 1, LocalDate.now().year, LocalDate.now().monthValue, 123.01))
    }

    @Test
    fun `should register new media usage and return all existing media for month`() {
        createMediaType(1, "POWER")
        createMediaType(2, "GAS")
        createMediaType(3, "ENERGY")
        createMedia(10, 2, meterRead = 222.11)

        mediaRepository.createMediaUsage(3, 123.01, LocalDate.now().year, LocalDate.now().monthValue).collectList()
            .block()!!
        val mediaReads =
            mediaRepository.createMediaUsage(1, 123.01, LocalDate.now().year, LocalDate.now().monthValue).collectList()
                .block()!!

        mediaReads.size shouldBe 3
        mediaReads shouldContainAll listOf(
            MediaItem(10, 2, LocalDate.now().year, LocalDate.now().monthValue, 222.11),
            MediaItem(2, 1, LocalDate.now().year, LocalDate.now().monthValue, 123.01),
            MediaItem(1, 3, LocalDate.now().year, LocalDate.now().monthValue, 123.01)
        )
    }
}