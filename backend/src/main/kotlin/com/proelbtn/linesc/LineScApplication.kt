package com.proelbtn.linesc

import com.proelbtn.linesc.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class LineScApplication

fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://localhost:3306/line-sc", driver = "com.mysql.jdbc.Driver", user = "root", password = "lineschool")

    transaction {
        create(UserGroupMessages, UserGroupRelations, UserGroups, UserMessages, UserRelations, Users)
    }

    runApplication<LineScApplication>(*args)
}
