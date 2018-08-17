package com.proelbtn.linesc

import com.proelbtn.linesc.interceptor.AuthorizationInterceptor
import com.proelbtn.linesc.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.handler.MappedInterceptor
import redis.clients.jedis.Jedis
import java.util.*


@Configuration
@SpringBootApplication
class LineScApplication {
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
