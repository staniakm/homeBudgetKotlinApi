package com.example.demo.service

import com.example.demo.entity.AuthRequest
import com.example.demo.entity.RegisterUserDto
import com.example.demo.entity.User
import com.example.demo.util.JwtTokenUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil
) {
    fun authenticate(authRequest: AuthRequest): String {
        val authenticate = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        )

        val user = authenticate.principal as UserDetails
        return jwtTokenUtil.generateAccessToken(user)
    }

    fun authenticate(user: User) = jwtTokenUtil.generateAccessToken(user)
}
