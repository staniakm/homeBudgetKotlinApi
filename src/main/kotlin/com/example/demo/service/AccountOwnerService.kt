package com.example.demo.service

import com.example.demo.entity.AccountOwner
import com.example.demo.entity.CreateOwnerRequest
import com.example.demo.repository.AccountOwnerRepository
import org.springframework.stereotype.Service

@Service
class AccountOwnerService(private val accountOwnerRepository: AccountOwnerRepository) {
    fun findAllOwners(): List<AccountOwner> = accountOwnerRepository.findAllOwners()

    fun createOwner(createOwnerRequest: CreateOwnerRequest) = accountOwnerRepository.createNewOwner(createOwnerRequest.name, createOwnerRequest.description)

}
