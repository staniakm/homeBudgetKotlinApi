package com.example.demo.service

import com.example.demo.entity.RegisterUserDto
import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserRegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(registerRequest: RegisterUserDto): Mono<User> {
        return User(registerRequest.username, passwordEncoder.encode(registerRequest.password))
            .let {
                userRepository.save(it)
            }
    }

}
