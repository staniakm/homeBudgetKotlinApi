package com.example.demo.configuration

import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import com.example.demo.util.JwtTokenUtil
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(private val jwtTokenUtil: JwtTokenUtil, private val userRepository: UserRepository) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: javax.servlet.http.HttpServletRequest,
        response: HttpServletResponse,
        chain: javax.servlet.FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header==null || header.isBlank() || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }

        // Get jwt token and validate

        // Get jwt token and validate
        val token = header.split(" ").toTypedArray()[1].trim { it <= ' ' }
        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response)
            return
        }

        // Get user identity and set it on the spring security context

        // Get user identity and set it on the spring security context
        val userDetails: User? = userRepository
            .findByName(jwtTokenUtil.getUsername(token)).block()


        val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails?.authorities ?: listOf()
        )

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

}