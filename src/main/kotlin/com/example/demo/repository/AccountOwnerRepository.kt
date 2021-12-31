package com.example.demo.repository

import com.example.demo.entity.AccountOwner
import com.example.demo.entity.accountOwnerMapper
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AccountOwnerRepository(private val helper: RepositoryHelper) {
    fun findAllOwners(): Flux<AccountOwner> {
        return helper.getList(SqlQueries.GET_ALL_OWNERS, accountOwnerMapper)
    }

    fun createNewOwner(name: String, description: String): Mono<AccountOwner> {
        return helper.executeUpdate(SqlQueries.ADD_NEW_OWNER) {
            bind("$1", name)
                .bind("$2", description)
        }.then(helper.findOne(SqlQueries.GET_OWNER_BY_NAME, accountOwnerMapper) {
            bind("$1", name)
        })
    }


}