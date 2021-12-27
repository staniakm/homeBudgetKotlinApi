package com.example.demo

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
open class IntegrationTest {

    @Autowired
    lateinit var client: DatabaseClient

    @AfterEach
    internal fun tearDown() {
        client.sql("delete from automatic_invoice_products ").then().block()
        client.sql("delete from income ").then().block()
        client.sql("delete from invoice_details ").then().block()
        client.sql("delete from invoice ").then().block()
        client.sql("delete from account ").then().block()
        client.sql("delete from account_owner ").then().block()
    }


    companion object {
        @Container
        val postgreSQLContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("schema.sql")

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
}
