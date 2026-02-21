package com.example.demo.controller

import com.example.demo.IntegrationTest
import com.example.demo.entity.MediaRegisterRequest
import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaType
import com.example.demo.entity.MediaTypeRequest
import com.example.demo.entity.MediaUsageResponse
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class MediaControllerTest : IntegrationTest() {

    @Test
    fun `should create media type and fetch by id`() {
        val created = restTemplate.postForEntity(
            "/api/media/type",
            MediaTypeRequest("water"),
            MediaType::class.java
        )

        created.statusCode shouldBe HttpStatus.OK
        created.body?.name shouldBe "WATER"

        val byId = restTemplate.getForEntity("/api/media/type/1", MediaType::class.java)
        byId.statusCode shouldBe HttpStatus.OK
        byId.body shouldBe MediaType(1, "WATER")
    }

    @Test
    fun `should return empty usage list when media type does not exist`() {
        val response = restTemplate.postForEntity(
            "/api/media/usage",
            MediaRegisterRequest(mediaType = 99, meterRead = 123.45, year = 2022, month = 11),
            Array<MediaUsageResponse>::class.java
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body?.size shouldBe 0
    }

    @Test
    fun `should register and delete media usage`() {
        createMediaType(1, "POWER")
        clockProvider.setTime("2022-11-20T00:00:00.00Z")

        val created = restTemplate.postForEntity(
            "/api/media/usage",
            MediaRegisterRequest(mediaType = 1, meterRead = 200.5, year = 2022, month = 11),
            Array<MediaUsageResponse>::class.java
        )

        created.statusCode shouldBe HttpStatus.OK
        created.body!!.toList() shouldContainAll listOf(MediaUsageResponse(1, 2022, 11, 200.5))

        val monthUsage = restTemplate.getForEntity("/api/media/usage?month=0", Array<MediaItem>::class.java)
        monthUsage.statusCode shouldBe HttpStatus.OK
        monthUsage.body?.size shouldBe 1

        val deleted = restTemplate.exchange(
            "/api/media/usage/1",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )
        deleted.statusCode shouldBe HttpStatus.OK

        val usageAfterDelete = restTemplate.getForEntity("/api/media/usage/1", Array<MediaUsageResponse>::class.java)
        usageAfterDelete.statusCode shouldBe HttpStatus.OK
        usageAfterDelete.body?.size shouldBe 0
    }
}
