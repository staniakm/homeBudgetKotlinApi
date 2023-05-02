package com.example.demo.repository

import com.example.demo.entity.AccountOwner
import com.example.demo.entity.accountOwnerMapper
import org.springframework.stereotype.Service

@Service
class AccountOwnerRepository(private val helper: RepositoryHelper) {
    fun findAllOwners(): List<AccountOwner> {
        return helper.jdbcQueryGetList(SqlQueries.GET_ALL_OWNERS, {}, accountOwnerMapper)
    }

    fun createNewOwner(name: String, description: String): AccountOwner? {
        val ownerId = helper.insertWithReturnKeyJdbc(SqlQueries.ADD_NEW_OWNER) {
            setString(1, name)
            setString(2, description)
        }

        return ownerId?.let {
            helper.jdbcQueryGetFirst(SqlQueries.GET_OWNER_BY_ID, {
                setLong(1, ownerId)
            }, accountOwnerMapper)!!
        }
    }
}