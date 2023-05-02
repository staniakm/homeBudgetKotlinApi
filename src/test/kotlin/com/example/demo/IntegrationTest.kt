package com.example.demo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.LocalDate
import java.util.logging.Logger

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
abstract class IntegrationTest {

    val logger = Logger.getLogger("[TEST LOGGER]")

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
        "media_type",
        "salary_type",
    )

    @Autowired
    private lateinit var client: JdbcTemplate

    @AfterEach
    internal fun tearDown() {
        tables.forEach { tableName ->
            client.update("truncate $tableName CASCADE; alter sequence ${tableName}_id_seq restart with 1;")
        }
    }

    companion object {
        @JvmStatic
        val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
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

            registry.add("spring.datasource.url") {
                java.lang.String.format(
                    "jdbc:postgresql://%s:%d/%s",
                    postgreSQLContainer.host,
                    postgreSQLContainer.firstMappedPort,
                    postgreSQLContainer.databaseName
                )
            }
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }

        }
    }

    fun executeInsert(query: String) {
        client.update(query)
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

    fun setup(description: String, op: () -> Unit) {
        logger.info(description)
        op.invoke()
        logger.info("Setup data created")
    }

    fun <T> methodUnderTest(description: String = "", op: () -> T): T {
        if (description.isNotBlank()) logger.info(description)
        return op.invoke()
    }

    fun validateResults(description: String = "", op: () -> Unit) {
        if (description.isNotBlank()) logger.info(description)
        op.invoke()
    }

    fun <T> validateResults(description: String = "", result: T, op: T.() -> Unit) {
        if (description.isNotBlank()) logger.info(description)
        op.invoke(result)
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

class AccountOwnerCreator {
    var description = ""
    var withId: Int? = null
        get() {
            if (field == null) {
                throw java.lang.IllegalArgumentException("Id is required")
            }
            return field
        }
    var withName: String? = null
        get() {
            if (field == null) {
                throw java.lang.IllegalArgumentException("Name is required")
            }
            return field
        }

    fun withEmptyDescription() {
        description = ""
    }
}