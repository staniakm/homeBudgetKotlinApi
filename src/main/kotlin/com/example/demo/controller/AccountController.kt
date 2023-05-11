package com.example.demo.controller

import com.example.demo.entity.*
import com.example.demo.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping("/api/account")
@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping
    fun getAccountsSummaryForMonth(
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ) = accountService.getAccountsSummaryForMonth(month)


    @GetMapping("/all")
    fun getAllAccounts(): ResponseEntity<List<Account>> = ResponseEntity.ok(accountService.findAll())

    @GetMapping("/{accountId}")
    fun getAccountOperations(
        @PathVariable accountId: Long,
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<List<ShoppingInvoice>> = ResponseEntity.ok(accountService.getAccountOperations(accountId, month))

    @GetMapping("/{accountId}/income")
    fun getAccountIncome(
        @PathVariable accountId: Int,
        @RequestParam("month", required = false, defaultValue = "0") month: Long
    ): ResponseEntity<List<AccountIncome>> = ResponseEntity.ok(accountService.getAccountIncome(accountId, month))


    @PutMapping("/{accountId}")
    fun updateAccountData(@PathVariable accountId: Int, @RequestBody updateAccount: UpdateAccountDto) =
        accountService.updateAccount(accountId, updateAccount)

    @PostMapping("/{accountId}")
    fun addAccountIncome(
        @PathVariable accountId: Long,
        @RequestBody updateAccount: AccountIncomeRequest
    ): ResponseEntity<List<AccountIncome>> {
        return ResponseEntity.ok(accountService.addAccountIncome(updateAccount))
    }

    @GetMapping("/income/type")
    fun getIncomeTypes(): ResponseEntity<List<IncomeType>> = ResponseEntity.ok(accountService.getIncomeTypes())

    @PutMapping("/{accountId}/transfer")
    fun transferMoney(@PathVariable accountId: Int, @RequestBody request: TransferMoneyRequest) =
        accountService.transferMoney(accountId, request)

    @GetMapping("/{accountId}/operations")
    fun getLastOperations(
        @PathVariable accountId: Int,
        @RequestParam(name = "limit", required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<List<AccountOperation>> {
        return ResponseEntity.ok(accountService.getOperations(accountId, limit))
    }

}
