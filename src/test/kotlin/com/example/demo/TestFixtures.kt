package com.example.demo

import java.math.BigDecimal
import java.time.LocalDate

fun IntegrationTest.givenOwnerAndPrimaryAccount(now: String = "2022-05-20T00:00:00.00Z") {
    clockProvider.setTime(now)
    createAccountOwner(1, "owner1")
    createAccount(1, BigDecimal("100.00"), "account1")
}

fun IntegrationTest.givenOwnerWithTwoAccounts() {
    createAccountOwner(1, "owner1")
    createAccount(1, BigDecimal("150.00"), "account1")
    createAccount(2, BigDecimal("110.00"), "account2")
}

fun IntegrationTest.seedCategoryBase(now: String = "2022-05-01T00:00:00.00Z") {
    clockProvider.setTime(now)
    createShop()
    createAccountOwner(1, "owner1")
    createAccount(1, BigDecimal.TEN)
}

fun IntegrationTest.seedThreeCategoriesWithAssortments() {
    createCategory(1, "category1")
    createCategory(2, "category2")
    createCategory(3, "category3")
    createAssortment(1, "assortment1", 1)
    createAssortment(2, "assortment2", 2)
    createAssortment(3, "assortment2", 3)
}

fun IntegrationTest.seedCategoryInvoicesForAprilAndMay() {
    createInvoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
    createInvoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
    createInvoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

    createInvoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
    createInvoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
    createInvoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)
}
