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
    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver ")
        dataSource.url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=finanse;user=apiUser;password=apiUserPass;integratedSecurity=false"
        return dataSource
    }

    @Bean
    fun jdbiTemplate(dataSource: DataSource): Jdbi {
        return Jdbi.create(dataSource).installPlugin(KotlinPlugin());
    }
}
