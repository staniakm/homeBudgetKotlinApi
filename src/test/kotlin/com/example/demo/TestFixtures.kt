package com.example.demo

import java.math.BigDecimal
import java.time.LocalDate

data class CatalogSeedItem(
    val assortmentId: Int,
    val assortmentName: String,
    val categoryId: Int,
    val categoryName: String
)

data class InvoiceItemSeed(
    val id: Int,
    val price: BigDecimal,
    val amount: BigDecimal,
    val unitPrice: BigDecimal,
    val discount: BigDecimal,
    val categoryId: Int,
    val assortmentId: Int
)

fun IntegrationTest.givenOwnerAndPrimaryAccount(now: String = "2022-05-20T00:00:00.00Z") {
    clockProvider.setTime(now)
    testDataBuilder.accountOwner(1, "owner1")
    testDataBuilder.account(1, BigDecimal("100.00"), "account1")
}

fun IntegrationTest.givenOwnerWithTwoAccounts() {
    testDataBuilder.accountOwner(1, "owner1")
    testDataBuilder.account(1, BigDecimal("150.00"), "account1")
    testDataBuilder.account(2, BigDecimal("110.00"), "account2")
}

fun IntegrationTest.givenDefaultFinanceContext() {
    testDataBuilder.accountOwner()
    testDataBuilder.account()
    testDataBuilder.shop()
}

fun IntegrationTest.givenCatalog(vararg items: CatalogSeedItem) {
    val categories = items.map { it.categoryId to it.categoryName }.distinctBy { it.first }
    categories.forEach { (id, name) -> testDataBuilder.category(id, name) }
    items.forEach { item -> testDataBuilder.assortment(item.assortmentId, item.assortmentName, item.categoryId) }
}

fun IntegrationTest.givenInvoiceWithItems(
    invoiceId: Int = 1,
    accountId: Int = 1,
    shopId: Int = 1,
    date: LocalDate = clockProvider.getDate(),
    items: List<InvoiceItemSeed>
) {
    testDataBuilder.invoice(invoiceId = invoiceId, accountId = accountId, date = date, amount = items.sumOf { it.price }, shopId = shopId)
    items.forEach { item ->
        testDataBuilder.invoiceItem(
            id = item.id,
            invoiceId = invoiceId,
            price = item.price,
            amount = item.amount,
            unitPrice = item.unitPrice,
            discount = item.discount,
            categoryId = item.categoryId,
            assortment = item.assortmentId
        )
    }
}

fun IntegrationTest.seedCategoryBase(now: String = "2022-05-01T00:00:00.00Z") {
    clockProvider.setTime(now)
    testDataBuilder.shop()
    testDataBuilder.accountOwner(1, "owner1")
    testDataBuilder.account(1, BigDecimal.TEN)
}

fun IntegrationTest.seedThreeCategoriesWithAssortments() {
    testDataBuilder.category(1, "category1")
    testDataBuilder.category(2, "category2")
    testDataBuilder.category(3, "category3")
    testDataBuilder.assortment(1, "assortment1", 1)
    testDataBuilder.assortment(2, "assortment2", 2)
    testDataBuilder.assortment(3, "assortment2", 3)
}

fun IntegrationTest.seedCategoryInvoicesForAprilAndMay() {
    testDataBuilder.invoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
    testDataBuilder.invoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
    testDataBuilder.invoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)

    testDataBuilder.invoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
    testDataBuilder.invoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
    testDataBuilder.invoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)
}
