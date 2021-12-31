package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaUsage
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
    fun `should register new media usage and return all media usage for type`() {
        createMediaType(1, "POWER")

        val mediaReads =
            mediaRepository.createMediaUsage(1, 123.01, LocalDate.now().year, LocalDate.now().monthValue).collectList()
                .block()!!

        mediaReads.size shouldBe 1
        mediaReads shouldContainAll listOf(MediaUsage(1, 1, LocalDate.now().year, LocalDate.now().monthValue, 123.01))
    }

    @Test
    fun `should register new media usage and return all existing media for type`() {
        createMediaType(1, "POWER")
        createMediaType(2, "GAS")
        createMediaType(3, "ENERGY")
        createMedia(10, 2, meterRead = 222.11)

        mediaRepository.createMediaUsage(1, 123.01, 2021, 11).collectList()
            .block()!!
        val mediaReads =
            mediaRepository.createMediaUsage(1, 124.01, 2021, 12).collectList()
                .block()!!

        mediaReads.size shouldBe 2
        mediaReads shouldContainAll listOf(
            MediaUsage(2, 1, 2021, 12, 124.01),
            MediaUsage(1, 1, 2021, 11, 123.01)
        )
    }

    @Test
    internal fun `should fetch list of media usage by media type`() {
        createMediaType(1, "POWER")
        createMedia(1, 1, meterRead = 122.11, year = 2021, month = 9)
        createMedia(2, 1, meterRead = 142.11, year = 2021, month = 10)
        createMedia(3, 1, meterRead = 162.11, year = 2021, month = 11)
        createMedia(4, 1, meterRead = 182.11, year = 2021, month = 12)

        val mediaUsage = mediaRepository.findByMediaType(1).collectList().block()!!

        mediaUsage.size shouldBe 4
        mediaUsage shouldContainAll listOf(
            MediaUsage(1, 1, 2021, 9, 122.11),
            MediaUsage(2, 1, 2021, 10, 142.11),
            MediaUsage(3, 1, 2021, 11, 162.11),
            MediaUsage(4, 1, 2021, 12, 182.11)
        )
    }
}