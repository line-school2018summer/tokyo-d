package com.proelbtn.linesc

import com.proelbtn.linesc.interceptor.AuthorizationInterceptor
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


@Configuration
@SpringBootApplication
@EnableSwagger2
class LineScApplication {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }

    @Bean
    fun interceptor(): MappedInterceptor {
        return MappedInterceptor(arrayOf("/**"), AuthorizationInterceptor())
    }
}

fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://localhost:3306/line-sc", driver = "com.mysql.jdbc.Driver", user = "root", password = "lineschool")

    transaction {
        create(UserGroupMessages, UserGroupRelations, UserGroups, UserMessages, UserRelations, Users)
    }

    runApplication<LineScApplication>(*args)
}
