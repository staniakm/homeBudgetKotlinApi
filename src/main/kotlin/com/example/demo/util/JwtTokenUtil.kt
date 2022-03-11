package com.example.demo.util

import com.example.demo.entity.User
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.lang.String.format
import java.security.SignatureException
import java.util.*


@Component
class JwtTokenUtil {
    private val jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP"
    private val jwtIssuer = "example.io"
    private val logger = LoggerFactory.getLogger(javaClass)
    fun generateAccessToken(user: UserDetails): String {
        return Jwts.builder()
            .setSubject(format("%s", user.username))
            .setIssuer(jwtIssuer)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserId(token: String?): String {
        val claims: Claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
        return claims.getSubject().split(",").get(0)
    }

    fun getUsername(token: String?): String {
        val claims: Claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
        return claims.subject
    }

    fun getExpirationDate(token: String?): Date {
        val claims: Claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
        return claims.getExpiration()
    }

    fun validate(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature - {}", ex.message)
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token - {}", ex.message)
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token - {}", ex.message)
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token - {}", ex.message)
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty - {}", ex.message)
        }
        return false
    }
}