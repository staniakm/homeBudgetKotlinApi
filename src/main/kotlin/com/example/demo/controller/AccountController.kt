package com.example.demo.controller

import com.example.demo.entity.Account
import com.example.demo.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/account")
@RestController
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping
    fun getAccountsSummaryForMonth(@RequestParam("month") month: Long): List<Account> {
        return accountService.getAccountsSummaryForMonth(month)
    }
}