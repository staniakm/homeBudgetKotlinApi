package com.example.demo.repository

import com.example.demo.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
    fun createMediaUsage(mediaTypeId: Int, meterRead: Double, year: Int, month: Int): Flux<MediaUsage> {
        return helper.executeUpdate(SqlQueries.ADD_NEW_MEDIA_USAGE) {
            bind("$1", mediaTypeId)
                .bind("$2", meterRead)
                .bind("$3", year)
                .bind("$4", month)
        }.thenMany(findByMediaType(mediaTypeId))

    }

    fun findByMediaType(it: Int): Flux<MediaUsage> {
        return helper.getList(SqlQueries.GET_MEDIA_USAGE_BY_TYPE, mediaUsageMapper) {
            bind("$1", it)
        }
    }

    fun deleteMediaUsageEntry(mediaUsageId: Int): Mono<Void> {
        return helper.executeUpdate(SqlQueries.DELETE_MEDIA_USAGE){
            bind("$1", mediaUsageId)
        }
    }

}