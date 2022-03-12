package com.example.demo.repository

import com.example.demo.entity.User
import com.example.demo.entity.userRowMapper
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserRepository(private val helper: RepositoryHelper) {

    fun findByName(username: String): Mono<User> {
//        return Mono.just(User("Mariusz", "test", 1))
        return helper.findOne(SqlQueries.FIND_USER_BY_NAME, userRowMapper) {
            bind("$1", username)
        }
    }

    fun save(it: User): Mono<User> {
        return helper.executeUpdate(SqlQueries.CREATE_USER) {
            bind("$1", it.name)
                .bind("$2", it.password)
        }.then(findByName(it.username))
    }
}

