package com.example.demo.controller

import com.example.demo.entity.Account
import com.example.demo.entity.MonthAccountSummary
import com.example.demo.entity.ShoppingInvoice
import com.example.demo.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/account")
@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping
    fun getAccountsSummaryForMonth(
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<List<MonthAccountSummary>> =
        ResponseEntity(accountService.getAccountsSummaryForMonth(month), HttpStatus.OK)


    @GetMapping("/all")
    fun getAllAccounts(): ResponseEntity<List<Account>> = ResponseEntity.ok(accountService.findAll())

    @GetMapping("/{accountId}")
    fun getAccountOperations(
        @PathVariable accountId: Long,
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<List<ShoppingInvoice>> = ResponseEntity.ok(accountService.getAccountOperations(accountId, month))
}
