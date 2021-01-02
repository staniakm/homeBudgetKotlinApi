package com.example.demo.config

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class Config {

    @Value("\${sql.server.url}")
    private lateinit var sqlUrl: String

    @Bean
    fun dataSource(): DataSource {
        return DriverManagerDataSource()
            .apply {
                setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                url = sqlUrl
            }
    }

    @Bean
    fun jdbiTemplate(dataSource: DataSource): Jdbi {
        return Jdbi.create(dataSource).installPlugin(KotlinPlugin());
    }
}
