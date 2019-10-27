package com.example.demo.service

import com.example.demo.entity.Account
import com.example.demo.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AccountService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    fun getAccountsSummaryForMonth(month: Long): List<Account> {
        return LocalDate.now().plusMonths(month)
                .let {
                    accountRepository.getAccountsSummaryForMonth(it)
                }
    }

}
