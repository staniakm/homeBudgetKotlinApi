package com.example.demo.repository

import com.example.demo.IntegrationTest
import com.example.demo.entity.MediaType
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class MediaTypeRepositoryTest(@Autowired private val mediaTypeRepository: MediaTypeRepository) : IntegrationTest() {


    @Test
    fun `should create new media type`() {
        val mediaType = mediaTypeRepository.registerNewMediaType("Water").block()!!

        mediaType.id shouldBe 1
        mediaType.name shouldBe "WATER"
    }

    @Test
    fun `should fetch existing media type by id`() {
        createMediaType(2, "POWER")

        val mediaType = mediaTypeRepository.findById(2).block()!!

        mediaType.id shouldBe 2
        mediaType.name shouldBe "POWER"
    }

    @Test
    fun `should fetch all media types`() {
        createMediaType(1, "WATER")
        createMediaType(2, "POWER")
        createMediaType(3, "GAS")

        val mediaTypes = mediaTypeRepository.findAll().collectList().block()!!

        mediaTypes.size shouldBe 3
        mediaTypes shouldContainAll listOf(
            MediaType(1, "WATER"),
            MediaType(2, "POWER"),
            MediaType(3, "GAS")
        )

    }
}