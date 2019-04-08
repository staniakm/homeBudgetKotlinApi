package com.example.demo.service

import com.example.demo.entity.BudgetItem
import com.example.demo.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.time.LocalDate
import java.util.*

@Service
class BudgetService {

    @Autowired
    lateinit var repository: Repository

    fun getMonthBudget(month: Long): BudgetItem {
        val date = LocalDate.now().plusMonths(month);
        return repository.getBudgetForMonth(date)
    }

}
