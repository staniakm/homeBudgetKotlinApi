package com.example.demo.controller

import com.example.demo.entity.AuthRequest
import com.example.demo.entity.RegisterUserDto
import com.example.demo.service.AuthService
import com.example.demo.service.UserRegisterService
import com.example.demo.util.JwtTokenUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
@RequestMapping("/api/user")
class LoginController(private val authService: AuthService, private val userRegisterService: UserRegisterService) {

    @GetMapping("/login")
    fun getString(): String {
        return "String"
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody authRequest: AuthRequest): ResponseEntity<String> {
        val token = authService.authenticate(authRequest)
        return ResponseEntity.ok()
            .header(
                HttpHeaders.AUTHORIZATION,
                token
            ).body(token)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody registerRequest: RegisterUserDto): Mono<ResponseEntity<String>> {
        return userRegisterService.registerUser(registerRequest)
            .map { user ->
                authService.authenticate(user).let { token ->
                    ResponseEntity.ok()
                        .header(
                            HttpHeaders.AUTHORIZATION,
                            token
                        ).body(token)
                }
            }
    }


}