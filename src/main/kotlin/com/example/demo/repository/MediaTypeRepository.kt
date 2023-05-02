package com.example.demo.repository

import com.example.demo.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MediaTypeRepository(private val helper: RepositoryHelper) {

    @Transactional(transactionManager = "transactionManager")
    fun registerNewMediaType(name: String): MediaType? {
        return helper.updateJdbc(SqlQueries.ADD_NEW_MEDIA_TYPE) {
            setString(1, name.uppercase())
        }.let {
            helper.jdbcQueryGetFirst(SqlQueries.FIND_MEDIA_TYPE_BY_NAME, {
                setString(1, name.uppercase())
            }, mediaTypeMapper)
        }
    }

    fun findById(id: Int): MediaType? {
        return helper.jdbcQueryGetFirst(SqlQueries.FIND_MEDIA_TYPE_BY_ID, {
            setInt(1, id)
        }, mediaTypeMapper)
    }

    fun findAll(): List<MediaType> {
        return helper.jdbcQueryGetList(SqlQueries.FIND_ALL_MEDIA_TYPES, {}, mediaTypeMapper)
    }
}

@Service
class MediaRepository(private val helper: RepositoryHelper) {
    fun getMediaForMonth(year: Int, month: Int): List<MediaItem> {
        return helper.jdbcQueryGetList(SqlQueries.GET_MEDIA_FOR_MONTH, {
            setInt(1, year)
            setInt(2, month)
        }, mediaMapper)
    }

    @Transactional(transactionManager = "transactionManager")
    fun createMediaUsage(mediaTypeId: Int, meterRead: Double, year: Int, month: Int): List<MediaUsage> {
        return helper.updateJdbc(SqlQueries.ADD_NEW_MEDIA_USAGE) {
            setInt(1, mediaTypeId)
            setDouble(2, meterRead)
            setInt(3, year)
            setInt(4, month)
        }.let {
            findByMediaType(mediaTypeId)
        }
    }

    fun findByMediaType(it: Int): List<MediaUsage> {
        return helper.jdbcQueryGetList(SqlQueries.GET_MEDIA_USAGE_BY_TYPE, {
            setInt(1, it)
        }, mediaUsageMapper)
    }

    fun deleteMediaUsageEntry(mediaUsageId: Int): Int {
        return helper.updateJdbc(SqlQueries.DELETE_MEDIA_USAGE) {
            setInt(1, mediaUsageId)
        }
    }

}