package com.example.demo.entity

import java.sql.ResultSet

data class AccountOwner(val id: Int, val name: String, val description: String)

val accountOwnerMapper: (row: ResultSet, _: Any?) -> AccountOwner = {row,_->
    AccountOwner(
        row.getInt("id") as Int,
        row.getString("owner_name") as String,
        row.getString("description") as String
    )
}

data class CreateOwnerRequest(val name: String, val description: String = "")