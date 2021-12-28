package com.example.demo

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Testcontainers
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
        "category"
    )

    @Autowired
    private lateinit var client: DatabaseClient

    @AfterEach
    internal fun tearDown() {
        tables.forEach { tableName ->
            client.sql("delete from $tableName").then().block()
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

    fun createShop() {
        executeInsert("insert into shop (id, name) values (1, 'shop name')")
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

    fun createAccount(accountId: Int = 1, amount: BigDecimal = BigDecimal.ONE, name: String = "account") {
        executeInsert("insert into account (id, account_name,description, money, owner) values ($accountId, '$name','desc',$amount, 1)")
    }

    fun createAccountOwner() {
        executeInsert("insert into account_owner (id, description, owner_name) values (1, 'owner', 'name')")
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
}
