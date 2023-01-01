package com.example.demo.entity

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata

data class AccountOwner(val id: Int, val name: String, val description: String)

val accountOwnerMapper: (row: Row, metadata: RowMetadata) -> AccountOwner = {row,_->
    AccountOwner(
        row["id"] as Int,
        row["owner_name"] as String,
        row["description"] as String
    )
}

data class CreateOwnerRequest(val name: String, val description: String = "")