package com.example.demo.controller

import com.example.demo.entity.AccountOwner
import com.example.demo.entity.CreateOwnerRequest
import com.example.demo.service.AccountOwnerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/owner")
class AccountOwnerController(private val accountOwnerService: AccountOwnerService) {

    @GetMapping("/")
    fun getAllOwners() = accountOwnerService.findAllOwners()

    @PostMapping("/")
    fun createOwner(@RequestBody accountOwnerRequest: CreateOwnerRequest): ResponseEntity<AccountOwner> {
        return accountOwnerService.createOwner(accountOwnerRequest)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.badRequest().build()
    }

}
