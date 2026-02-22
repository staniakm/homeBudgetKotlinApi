package com.example.demo.fixtures

import java.math.BigDecimal
import java.time.LocalDate

class TestDataBuilder(private val fixtures: TestDataFixtures) {

    fun shop(shopId: Int = 1, shopName: String = "ShopName") = fixtures.createShop(shopId, shopName)

    fun autoinvoiceEntry(
        asoId: Int = 1,
        price: BigDecimal = BigDecimal.ONE,
        quantity: BigDecimal = BigDecimal.ONE,
        shopId: Int = 8,
        accountId: Int = 3
    ) = fixtures.createAutoinvoiceEntry(asoId, price, quantity, shopId, accountId)

    fun invoice(
        invoiceId: Int = 1,
        accountId: Int = 1,
        date: LocalDate = fixtures.today(),
        amount: BigDecimal = BigDecimal.TEN,
        shopId: Int = 1
    ) = fixtures.createInvoice(invoiceId, accountId, date, amount, shopId)

    fun invoiceItem(
        id: Int,
        invoiceId: Int,
        price: BigDecimal,
        amount: BigDecimal,
        unitPrice: BigDecimal,
        discount: BigDecimal,
        categoryId: Int,
        assortment: Int
    ) = fixtures.createInvoiceItem(id, invoiceId, price, amount, unitPrice, discount, categoryId, assortment)

    fun assortment(id: Int, name: String, category: Int) = fixtures.createAssortment(id, name, category)

    fun income(accountId: Int = 1, amount: BigDecimal, date: LocalDate) = fixtures.createIncome(accountId, amount, date)

    fun incomeWithId(id: Int, accountId: Int = 1, amount: BigDecimal, date: LocalDate) =
        fixtures.createIncomeWithId(id, accountId, amount, date)

    fun account(accountId: Int = 1, amount: BigDecimal = BigDecimal.ONE, name: String = "account") =
        fixtures.createAccount(accountId, amount, name)

    fun incomeType(id: Int = 1, name: String = "Income") = fixtures.createIncomeType(id, name)

    fun accountOwner(id: Int = 1, name: String = "Owner name", description: String = "") =
        fixtures.createAccountOwner(id, name, description)

    fun accountOwner(op: AccountOwnerCreator.() -> Unit) = fixtures.accountOwnerCreator(op)

    fun category(categoryId: Int = 1, categoryName: String = "Unknown") = fixtures.createCategory(categoryId, categoryName)

    fun salaryIncomeType(id: Int = 1, name: String = "Sallary") = fixtures.createSalaryIncomeType(id, name)

    fun budgetItem(
        id: Int = 1,
        categoryId: Int = 1,
        month: Int = fixtures.today().monthValue,
        year: Int = fixtures.today().year,
        planned: BigDecimal = BigDecimal.ZERO,
        used: BigDecimal = BigDecimal.ZERO,
        percentage: Int = 0
    ) = fixtures.createBudgetItem(id, categoryId, month, year, planned, used, percentage)

    fun mediaType(id: Int = 1, name: String = "MEDIA") = fixtures.createMediaType(id, name)

    fun media(
        id: Int = 1,
        mediaTypeId: Int = 1,
        year: Int = fixtures.today().year,
        month: Int = fixtures.today().monthValue,
        meterRead: Double = 0.0
    ) = fixtures.createMedia(id, mediaTypeId, year, month, meterRead)

    fun shopItem(shopId: Int = 1, asoId: Int = 1) = fixtures.createShopItem(shopId, asoId)
}
