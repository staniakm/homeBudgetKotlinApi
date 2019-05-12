package com.example.demo.entity

import java.math.BigDecimal

data class Account (val id: Long, val name: String, val moneyAmount: BigDecimal, val expense: BigDecimal, val income: BigDecimal) {

}
