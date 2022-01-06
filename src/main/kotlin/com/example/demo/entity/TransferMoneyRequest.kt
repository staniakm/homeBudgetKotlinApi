package com.example.demo.entity

import java.math.BigDecimal

data class TransferMoneyRequest(val accountId: Int, val value: BigDecimal, val targetAccount: Int)