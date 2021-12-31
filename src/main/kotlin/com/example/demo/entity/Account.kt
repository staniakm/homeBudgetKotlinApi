package com.example.demo.entity

import io.r2dbc.spi.Row

data class AccountOwner(val id: Int, val name: String, val description: String)

val accountOwnerMapper: (row: Row) -> AccountOwner = {
    AccountOwner(
        it["id"] as Int,
        it["owner_name"] as String,
        it["description"] as String
    )
}

data class CreateOwnerRequest(val name: String, val description: String = "")