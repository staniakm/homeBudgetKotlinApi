package com.example.demo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
abstract class IntegrationTest {

    val tables = listOf(
        "automatic_invoice_products",
        "income",
        "invoice_details",
        "invoice",
        "account",
        "account_owner",
        "shop",
        "budget",
        "assortment",
        "category",
        "media_usage",
        "media_type"
    )

    @Autowired
    private lateinit var client: DatabaseClient

    @AfterEach
    internal fun tearDown() {
        tables.forEach { tableName ->
            client.sql("truncate $tableName CASCADE; alter sequence ${tableName}_id_seq restart with 1;").then().block()
        }
    }

    companion object {
        @JvmStatic
        val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("schema.sql")

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            postgreSQLContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {

            val jdbcUrl: String = postgreSQLContainer.getJdbcUrl()
            registry.add("spring.liquibase.url") { jdbcUrl }
            registry.add("spring.liquibase.user", postgreSQLContainer::getUsername)
            registry.add("spring.liquibase.password", postgreSQLContainer::getPassword)

            registry.add("spring.r2dbc.url") {
                java.lang.String.format(
                    "r2dbc:pool:postgresql://%s:%d/%s",
                    postgreSQLContainer.getHost(),
                    postgreSQLContainer.getFirstMappedPort(),
                    postgreSQLContainer.getDatabaseName()
                )
            }
            registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername)
            registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword)
        }
    }

    fun executeInsert(query: String) {
        client.sql(query).then().block()
    }

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
        date: LocalDate = LocalDate.now(),
        amount: BigDecimal = BigDecimal.TEN
    ) {
        executeInsert("insert into invoice(id, date, invoice_number, sum, description, account, shop) values ($invoiceId, '$date', '1a', $amount, '', $accountId, 1)")
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

    fun createAccountOwner(id: Int = 1, name: String = "Owner name", description: String = "") {
        executeInsert("insert into account_owner (id, owner_name,description) values ($id, '$name', '$description')")
    }

    fun createCategory(categoryId: Int = 1, categoryName: String = "Unknown") {
        executeInsert("insert into category(id, name) values ($categoryId, '$categoryName')")
    }

    fun createBudgetItem(
        id: Int = 1,
        categoryId: Int = 1,
        month: Int = LocalDate.now().monthValue,
        year: Int = LocalDate.now().year,
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
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue,
        meterRead: Double = 0.0
    ) {
        executeInsert("insert into media_usage(id, media_type, year, month, meter_read) values ($id, $mediaTypeId, $year, $month, $meterRead)")
    }

    fun createShopItem(shopId: Int = 1, asoId: Int = 1) {
        executeInsert("insert into shop_assortment(shop, aso) values ($shopId, $asoId)")
    }
}
