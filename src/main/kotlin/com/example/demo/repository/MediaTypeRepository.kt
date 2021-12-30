package com.example.demo.repository

import com.example.demo.entity.MediaItem
import com.example.demo.entity.MediaType
import com.example.demo.entity.mediaMapper
import com.example.demo.entity.mediaTypeMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class MediaTypeRepository(private val helper: RepositoryHelper) {

    @Transactional
    fun registerNewMediaType(name: String): Mono<MediaType> {
        return helper.executeUpdate(SqlQueries.ADD_NEW_MEDIA_TYPE) {
            bind("$1", name.uppercase())
        }.then(helper.findOne(SqlQueries.FIND_MEDIA_TYPE_BY_NAME, mediaTypeMapper) {
            bind("$1", name.uppercase())
        })
    }

    fun findById(id: Int): Mono<MediaType> {
        return helper.findOne(SqlQueries.FIND_MEDIA_TYPE_BY_ID, mediaTypeMapper) {
            bind("$1", id)
        }
    }

    fun findAll(): Flux<MediaType> {
        return helper.getList(SqlQueries.FIND_ALL_MEDIA_TYPES, mediaTypeMapper)
    }
}

@Service
class MediaRepository(private val helper: RepositoryHelper) {
    fun getMediaForMonth(year: Int, month: Int): Flux<MediaItem> {
        return helper.getList(SqlQueries.GET_MEDIA_READS_FOR_MONTH, mediaMapper) {
            bind("$1", year)
                .bind("$2", month)
        }
    }
    @Transactional
    fun createMediaUsage(mediaTypeId: Int, meterRead: Double, year: Int, month: Int): Flux<MediaItem> {
        return helper.executeUpdate(SqlQueries.ADD_NEW_MEDIA_USAGE) {
            bind("$1", mediaTypeId)
                .bind("$2", meterRead)
                .bind("$3", year)
                .bind("$4", month)
        }.thenMany(getMediaForMonth(year, month))

    }

}