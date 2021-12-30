package com.example.demo.repository

import com.example.demo.entity.MediaType
import com.example.demo.entity.mediaTypeMapper
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MediaTypeRepository(private val helper: RepositoryHelper) {

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