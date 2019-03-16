package com.example.demo.entity

import java.math.BigDecimal
import java.sql.Date

data class ShoppingList(val id:Long, val name:String, val date: Date, val price: BigDecimal) {

}
