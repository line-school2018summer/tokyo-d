package com.proelbtn.linesc

import com.proelbtn.linesc.interceptor.AuthenticationInterceptor
import com.proelbtn.linesc.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.handler.MappedInterceptor
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact

@Configuration
@SpringBootApplication
@EnableSwagger2
class LineScApplication {

    @Bean
    fun api(): Docket {
        ApiInfoBuilder()
        val info: ApiInfo = ApiInfoBuilder()
                .title("TRIO API")
                .description("This documentation is for TRIO.")
                .build()
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(info)
    }

    @Bean
    fun interceptor(): MappedInterceptor {
        return MappedInterceptor(arrayOf("/**"), AuthenticationInterceptor())
    }
}

fun main(args: Array<String>) {
    val config = HikariConfig()
    config.jdbcUrl = "jdbc:mysql://localhost:3306/linesc?allowPublicKeyRetrieval=true&useSSL=no"
    config.username = "root"
    config.password = "lineschool"

    val ds = HikariDataSource(config)

    Database.connect(ds)

    transaction {
        create(UserGroupMessages, UserGroupRelations, UserGroups, UserMessages, UserRelations, Users)
    }

    runApplication<LineScApplication>(*args)
}
