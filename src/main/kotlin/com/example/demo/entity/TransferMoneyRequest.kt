package com.example.demo.entity

import java.math.BigDecimal

data class TransferMoneyRequest(val accountId: Long, val value: BigDecimal, val targetAccount: Long)