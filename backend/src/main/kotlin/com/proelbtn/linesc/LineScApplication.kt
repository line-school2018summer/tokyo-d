package com.proelbtn.linesc

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.mysql.jdbc.jdbc2.optional.MysqlPooledConnection
import com.proelbtn.linesc.interceptor.AuthenticationInterceptor
import com.proelbtn.linesc.model.*
import org.apache.commons.dbcp2.BasicDataSource
import org.apache.commons.dbcp2.BasicDataSourceFactory
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
import java.util.*
import javax.sql.DataSource


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
        return MappedInterceptor(arrayOf("/**"), AuthenticationInterceptor())
    }
}

fun main(args: Array<String>) {
    val props = Properties()
    props.setProperty("url", "jdbc:mysql://localhost:3306/line-sc?useSSL=false")
    props.setProperty("driverClassName", "com.mysql.jdbc.Driver")
    props.setProperty("username", "root")
    props.setProperty("password", "lineschool")

    val ds = BasicDataSourceFactory.createDataSource(props)
    ds.initialSize = 16

    Database.connect(ds)

    transaction {
        create(UserGroupMessages, UserGroupRelations, UserGroups, UserMessages, UserRelations, Users)
    }

    runApplication<LineScApplication>(*args)
}
