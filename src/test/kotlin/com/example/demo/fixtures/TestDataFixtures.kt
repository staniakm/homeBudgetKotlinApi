package com.example.demo.fixtures

import java.math.BigDecimal
import java.time.LocalDate

abstract class TestDataFixtures {

    protected abstract fun executeInsert(query: String)
    protected abstract fun currentDate(): LocalDate

    fun today(): LocalDate = currentDate()

    fun createShop(shopId: Int = 1, shopName: String = "ShopName") {
        executeInsert("insert into shop (id, name) values ($shopId, '$shopName')")
    }

    fun createAutoinvoiceEntry(
        asoId: Int = 1,
        price: BigDecimal = BigDecimal.ONE,
        quantity: BigDecimal = BigDecimal.ONE,
        shopId: Int = 8,
        accountId: Int = 3
    ) {
        executeInsert("insert into automatic_invoice_products (aso, price, quantity, shop, account) values ($asoId, $price, $quantity, $shopId, $accountId)")
    }

    fun createInvoice(
        invoiceId: Int = 1,
        accountId: Int = 1,
        date: LocalDate = currentDate(),
        amount: BigDecimal = BigDecimal.TEN,
        shopId: Int = 1
    ) {
        executeInsert("insert into invoice(id, date, invoice_number, sum, description, account, shop) values ($invoiceId, '$date', '1a', $amount, '', $accountId, $shopId)")
    }

    fun createInvoiceItem(
        id: Int,
        invoiceId: Int,
        price: BigDecimal,
        amount: BigDecimal,
        unitPrice: BigDecimal,
        discount: BigDecimal,
        categoryId: Int,
        assortment: Int
    ) {
        executeInsert(
            """insert into invoice_details(id, invoice, price, amount, unit_price, discount, category, assortment)
                                                    values ($id, $invoiceId, $price, $amount, $unitPrice, $discount, $categoryId, $assortment)"""
        )
    }

    fun createAssortment(id: Int, name: String, category: Int) {
        executeInsert("insert into assortment(id, name, category) values ($id, '$name', $category)")
    }

    fun createIncome(accountId: Int = 1, amount: BigDecimal, date: LocalDate) {
        executeInsert("insert into income(value, description, account, date) values ( $amount, 'Income', $accountId, '$date')")
    }

    fun createIncomeWithId(id: Int, accountId: Int = 1, amount: BigDecimal, date: LocalDate) {
        executeInsert("insert into income(id, value, description, account, date) values ($id, $amount, 'Income', $accountId, '$date')")
    }

    fun createAccount(accountId: Int = 1, amount: BigDecimal = BigDecimal.ONE, name: String = "account") {
        executeInsert("insert into account (id, account_name,description, money, owner) values ($accountId, '$name','desc',$amount, 1)")
    }

    fun createIncomeType(id: Int = 1, name: String = "Income") {
        executeInsert("insert into salary_type(id, name) values ($id, '$name')")
    }

    fun createAccountOwner(id: Int = 1, name: String = "Owner name", description: String = "") {
        executeInsert("insert into account_owner (id, owner_name,description) values ($id, '$name', '$description')")
    }

    fun createCategory(categoryId: Int = 1, categoryName: String = "Unknown") {
        executeInsert("insert into category(id, name) values ($categoryId, '$categoryName')")
    }

    fun accountOwnerCreator(op: AccountOwnerCreator.() -> Unit) {
        with(AccountOwnerCreator()) {
            op.invoke(this)
            createAccountOwner(withId!!, withName!!, description)
        }
    }

    fun createSalaryIncomeType(id: Int = 1, name: String = "Sallary") {
        executeInsert("insert into salary_type(id, name) values ($id, '$name')")
    }

    fun createBudgetItem(
        id: Int = 1,
        categoryId: Int = 1,
        month: Int = currentDate().monthValue,
        year: Int = currentDate().year,
        planned: BigDecimal = BigDecimal.ZERO,
        used: BigDecimal = BigDecimal.ZERO,
        percentage: Int = 0
    ) {
        executeInsert("insert into budget (id, category, month, year, planned, used, percentage) values ($id, $categoryId, $month, $year, $planned, $used, $percentage)")
    }

    fun createMediaType(
        id: Int = 1,
        name: String = "MEDIA"
    ) {
        executeInsert("insert into media_type(id, name) values ($id, '$name')")
    }

    fun createMedia(
        id: Int = 1,
        mediaTypeId: Int = 1,
        year: Int = currentDate().year,
        month: Int = currentDate().monthValue,
        meterRead: Double = 0.0
    ) {
        executeInsert("insert into media_usage(id, media_type, year, month, meter_read) values ($id, $mediaTypeId, $year, $month, $meterRead)")
    }

    fun createShopItem(shopId: Int = 1, asoId: Int = 1) {
        executeInsert("insert into shop_assortment(shop, aso) values ($shopId, $asoId)")
    }
}

class AccountOwnerCreator {
    var description = ""
    var withId: Int? = null
        get() {
            if (field == null) {
                throw IllegalArgumentException("Id is required")
            }
            return field
        }
    var withName: String? = null
        get() {
            if (field == null) {
                throw IllegalArgumentException("Name is required")
            }
            return field
        }

    fun withEmptyDescription() {
        description = ""
    }
}
