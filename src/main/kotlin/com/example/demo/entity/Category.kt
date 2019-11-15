package com.example.demo.entity

import java.math.BigDecimal

class Category(val id: Long, val name: String, var monthSummary: BigDecimal,
               val yearSummary: BigDecimal, var details: List<CategoryDetails>)