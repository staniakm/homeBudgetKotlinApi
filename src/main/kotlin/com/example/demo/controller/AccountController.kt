package com.example.demo.controller

import com.example.demo.entity.Account
import com.example.demo.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/account")
@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping
    fun getAccountsSummaryForMonth(@RequestParam("month") month: Long): ResponseEntity<List<Account>> {
        return ResponseEntity(accountService.getAccountsSummaryForMonth(month), HttpStatus.OK)
    }
}