package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@CrossOrigin
@RequestMapping("/api/account")
@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping
    fun getAccountsSummaryForMonth(
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ) = accountService.getAccountsSummaryForMonth(month)


    @GetMapping("/all")
    fun getAllAccounts(): Flux<Account> = accountService.findAll()

    @GetMapping("/{accountId}")
    fun getAccountOperations(
        @PathVariable accountId: Long,
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): Flux<ShoppingInvoice> = accountService.getAccountOperations(accountId, month)

    @PutMapping("/{accountId}")
    fun updateAccountMoneyAmount(@PathVariable accountId: Long, @RequestBody updateAccount: UpdateAccountDto) =
        accountService.updateAccount(accountId, updateAccount)
}
