package com.example.demo

import com.example.demo.config.TestConfig
import com.example.demo.fixtures.TestDataBuilder
import com.example.demo.fixtures.TestDataFixtures
import com.example.demo.fixtures.TestDatabaseCleanup
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate
import java.util.logging.Logger

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestConfig::class])
@Testcontainers
@ActiveProfiles("test")
abstract class IntegrationTest : TestDataFixtures() {

    val testDataBuilder = TestDataBuilder(this)

    @Autowired
    private lateinit var client: JdbcTemplate

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var clockProvider: FakeClockProvider

    @AfterEach
    internal fun tearDown() {
        TestDatabaseCleanup.tables.forEach { tableName ->
            client.update("truncate $tableName CASCADE; alter sequence ${tableName}_id_seq restart with 1;")
        }
        clockProvider.resetTime()
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

    override fun executeInsert(query: String) {
        Logger.getLogger("[TEST LOGGER]").info("Executing query: $query")
        client.update(query)
    }

    override fun currentDate(): LocalDate = clockProvider.getDate()

    override fun setTime(timestamp: String) {
        clockProvider.setTime(timestamp)
    }
}
