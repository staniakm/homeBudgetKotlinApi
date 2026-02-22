package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaUsage
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MediaRepositoryTest(@Autowired private val mediaRepository: MediaRepository) : IntegrationTest() {

    @Test
    fun `should fetch media for selected month`() {
        testDataBuilder.mediaType(1, "POWER")
        testDataBuilder.mediaType(2, "GAS")
        testDataBuilder.media(id = 1, mediaTypeId = 1, meterRead = 11.00)
        testDataBuilder.media(2, mediaTypeId = 2, meterRead = 123.00)

        val now = clockProvider.getDate()

        val mediaListItems =
            mediaRepository.getMediaForMonth(now.year, now.monthValue)

        mediaListItems.size shouldBe 2
        mediaListItems shouldContainAll listOf(
            MediaItem(1, 1, now.year, now.monthValue, 11.00),
            MediaItem(2, 2, now.year, now.monthValue, 123.00)
        )
    }

    @Test
    fun `should register new media usage and return all media usage for type`() {
        testDataBuilder.mediaType(1, "POWER")

        val now = clockProvider.getDate()

        val mediaReads =
            mediaRepository.createMediaUsage(1, 123.01, now.year, now.monthValue)

        mediaReads.size shouldBe 1
        mediaReads shouldContainAll listOf(MediaUsage(1, 1, now.year, now.monthValue, 123.01))
    }

    @Test
    fun `should register new media usage and return all existing media for type`() {
        testDataBuilder.mediaType(1, "POWER")
        testDataBuilder.mediaType(2, "GAS")
        testDataBuilder.mediaType(3, "ENERGY")
        testDataBuilder.media(10, 2, meterRead = 222.11)

        mediaRepository.createMediaUsage(1, 123.01, 2021, 11)
        val mediaReads =
            mediaRepository.createMediaUsage(1, 124.01, 2021, 12)

        mediaReads.size shouldBe 2
        mediaReads shouldContainAll listOf(
            MediaUsage(2, 1, 2021, 12, 124.01),
            MediaUsage(1, 1, 2021, 11, 123.01)
        )
    }

    @Test
    internal fun `should fetch list of media usage by media type`() {
        testDataBuilder.mediaType(1, "POWER")
        testDataBuilder.media(1, 1, meterRead = 122.11, year = 2021, month = 9)
        testDataBuilder.media(2, 1, meterRead = 142.11, year = 2021, month = 10)
        testDataBuilder.media(3, 1, meterRead = 162.11, year = 2021, month = 11)
        testDataBuilder.media(4, 1, meterRead = 182.11, year = 2021, month = 12)

        val mediaUsage = mediaRepository.findByMediaType(1)

        mediaUsage.size shouldBe 4
        mediaUsage shouldContainAll listOf(
            MediaUsage(1, 1, 2021, 9, 122.11),
            MediaUsage(2, 1, 2021, 10, 142.11),
            MediaUsage(3, 1, 2021, 11, 162.11),
            MediaUsage(4, 1, 2021, 12, 182.11)
        )
    }

    @Test
    internal fun `should delete media usage`() {
        testDataBuilder.mediaType(1, "POWER")
        testDataBuilder.media(1, 1)

        mediaRepository.deleteMediaUsageEntry(1)

        val media = mediaRepository.findByMediaType(1)

        media.size shouldBe 0
    }
}
