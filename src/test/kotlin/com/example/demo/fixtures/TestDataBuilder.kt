package com.example.demo.fixtures

import com.example.demo.CatalogSeedItem
import com.example.demo.InvoiceItemSeed
import java.math.BigDecimal
import java.time.LocalDate

class TestDataBuilder(private val fixtures: TestDataFixtures) {

    fun givenOwnerAndPrimaryAccount(now: String = "2022-05-20T00:00:00.00Z") {
        fixtures.setTime(now)
        accountOwner(1, "owner1")
        account(1, BigDecimal("100.00"), "account1")
    }

    fun givenOwnerWithTwoAccounts() {
        accountOwner(1, "owner1")
        account(1, BigDecimal("150.00"), "account1")
        account(2, BigDecimal("110.00"), "account2")
    }

    fun givenDefaultFinanceContext() {
        accountOwner()
        account()
        shop()
    }

    fun givenCatalog(vararg items: CatalogSeedItem) {
        val categories = items.map { it.categoryId to it.categoryName }.distinctBy { it.first }
        categories.forEach { (id, name) -> category(id, name) }
        items.forEach { item -> assortment(item.assortmentId, item.assortmentName, item.categoryId) }
    }

    fun givenInvoiceWithItems(
        invoiceId: Int = 1,
        accountId: Int = 1,
        shopId: Int = 1,
        date: LocalDate = fixtures.today(),
        items: List<InvoiceItemSeed>
    ) {
        invoice(
            invoiceId = invoiceId,
            accountId = accountId,
            date = date,
            amount = items.sumOf { it.price },
            shopId = shopId
        )
        items.forEach { item ->
            invoiceItem(
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

    fun givenInvoiceWithTwoItems(
        invoiceId: Int = 1,
        accountId: Int = 1,
        shopId: Int = 1,
        date: LocalDate = fixtures.today(),
        firstItem: InvoiceItemSeed,
        secondItem: InvoiceItemSeed
    ) {
        givenInvoiceWithItems(
            invoiceId = invoiceId,
            accountId = accountId,
            shopId = shopId,
            date = date,
            items = listOf(firstItem, secondItem)
        )
    }

    fun givenShopWithAssortment(
        shopId: Int = 1,
        shopName: String = "ShopName",
        assortmentId: Int = 1,
        assortmentName: String = "item",
        categoryId: Int = 1,
        categoryName: String = "Unknown",
        createShop: Boolean = true,
        createCategory: Boolean = true,
        linkToShop: Boolean = true
    ) {
        if (createShop) shop(shopId, shopName)
        if (createCategory) category(categoryId, categoryName)
        assortment(assortmentId, assortmentName, categoryId)
        if (linkToShop) shopItem(shopId, assortmentId)
    }

    fun givenAccountWithIncome(
        ownerId: Int = 1,
        ownerName: String = "owner1",
        accountId: Int = 1,
        accountName: String = "account1",
        accountAmount: BigDecimal = BigDecimal("100.00"),
        incomes: List<Pair<BigDecimal, LocalDate>>
    ) {
        accountOwner(ownerId, ownerName)
        account(accountId, accountAmount, accountName)
        incomes.forEach { (amount, date) -> income(accountId, amount, date) }
    }

    fun givenCategoryBase(now: String = "2022-05-01T00:00:00.00Z") {
        fixtures.setTime(now)
        shop()
        accountOwner(1, "owner1")
        account(1, BigDecimal.TEN)
    }

    fun givenThreeCategoriesWithAssortments() {
        category(1, "category1")
        category(2, "category2")
        category(3, "category3")
        assortment(1, "assortment1", 1)
        assortment(2, "assortment2", 2)
        assortment(3, "assortment2", 3)
    }

    fun givenCategoryInvoicesForAprilAndMay() {
        invoice(1, 1, LocalDate.of(2022, 4, 1), BigDecimal.TEN, 1)
        invoiceItem(1, 1, BigDecimal("10.1"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        invoiceItem(4, 1, BigDecimal("100.00"), BigDecimal.ONE, BigDecimal("100"), BigDecimal.ZERO, 3, 3)
        invoice(2, 1, LocalDate.of(2022, 5, 1), BigDecimal("20.10"), 1)
        invoiceItem(2, 2, BigDecimal("20.10"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1, 1)
        invoiceItem(3, 2, BigDecimal("10.00"), BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 2, 2)
    }

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
