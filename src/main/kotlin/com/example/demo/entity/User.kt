package com.example.demo.entity

import io.r2dbc.spi.Row
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(val name: String, val userPassword: String, val id: Int? = null) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return userPassword
    }

    override fun getUsername(): String {
        return name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

val userRowMapper: (row: Row) -> User = { row ->
    User(
        row["username"] as String,
        row["password"] as String,
        row["id"] as Int
    )
}
